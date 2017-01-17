package com.caco3.mvk.network;

import com.caco3.mvk.util.io.Closeables;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.Okio;
import okio.Sink;
import okio.Source;
import rx.Observable;
import rx.Subscriber;

import static com.caco3.mvk.util.Preconditions.checkArgument;
import static com.caco3.mvk.util.Preconditions.checkNotNull;

/**
 * Usage:
 * 1) create instance of this class {@link #FileDownloader(OkHttpClient, HttpUrl, File)}.
 * 2) call {@link #download()}.
 * 3) subscribe to {@link Observable} returned by {@link #download()}.
 *
 * If any error occurs during download this class will do nothing with provided file.
 * If file exists and it's content is not empty file will be overwritten
 */
public class FileDownloader {
  private static final int CHUNK_SIZE = 1024 * 8;

  private final OkHttpClient okHttpClient;
  private File downloadInto;
  private HttpUrl url;

  private Response response;
  private Source source;
  private Sink sink;
  private Buffer buffer = new Buffer();

  private DownloadProgress downloadProgress = new DownloadProgress();

  /**
   * @param okHttpClient download with
   * @param url download from
   * @param downloadInto download into. It must exist
   * @throws NullPointerException if any of argument is null.
   * @throws IllegalArgumentException if  {@code downloadInto} file not exists
   */
  public FileDownloader(OkHttpClient okHttpClient, HttpUrl url, File downloadInto) {
    this.okHttpClient = checkNotNull(okHttpClient, "okHttpClient == null");
    this.url = checkNotNull(url, "url == null");
    this.downloadInto = checkNotNull(downloadInto, "downloadInto");
    checkArgument(downloadInto.exists(), String.format("File not exists (%s)", downloadInto));
  }


  public Observable<DownloadProgress> download() {
    return Observable.create(new Observable.OnSubscribe<DownloadProgress>() {
      @Override
      public void call(Subscriber<? super DownloadProgress> subscriber) {
        try {
          long startNanos = System.nanoTime();
          prepareDownload();
          long bytesReadTotal = 0;
          long contentLength = response.body().contentLength();
          long read;

          while ((read = source.read(buffer, CHUNK_SIZE)) != -1) {
            sink.write(buffer, read);
            long nanosElapsed = System.nanoTime() - startNanos;
            bytesReadTotal += read;
            updateProgress(contentLength, bytesReadTotal, nanosElapsed);
            subscriber.onNext(downloadProgress);
          }
          subscriber.onCompleted();
        } catch (Throwable t) {
          subscriber.onError(t);
        } finally {
          cleanUp();
        }
      }
    });
  }

  private void prepareDownload() throws IOException {
    Response response = makeRequest();
    openSource(response);
    openSink();

    this.response = response;
  }

  private void updateProgress(long totalBytes,
                              long bytesDownloaded, long nanosElapsed) {
    downloadProgress.totalBytes = totalBytes;
    downloadProgress.bytesDownloaded = bytesDownloaded;
    downloadProgress.nanosElapsed = nanosElapsed;
  }

  private Response makeRequest() throws IOException {
    Request request = new Request.Builder().url(url).build();
    Response response = okHttpClient.newCall(request).execute();
    if (!response.isSuccessful()) {
      throw new IOException("Unexpected code: " + response);
    }

    return response;
  }

  private void openSink() throws IOException {
    sink = Okio.sink(downloadInto);
  }

  private void openSource(Response response) {
    source = Okio.source(response.body().byteStream());
  }

  private void cleanUp() {
    Closeables.closeOrLog(sink);
    Closeables.closeOrLog(source);
    Closeables.closeOrLog(response);
  }

  public static class DownloadProgress {
    private long totalBytes;
    private long bytesDownloaded;
    private long nanosElapsed;

    public long getTotalBytes() {
      return totalBytes;
    }

    public long getBytesDownloaded() {
      return bytesDownloaded;
    }

    public long getNanosElapsed() {
      return nanosElapsed;
    }
  }
}
