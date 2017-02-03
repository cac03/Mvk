package com.caco3.mvk.audiodownload;

import com.caco3.mvk.vk.audio.AudiosGenerator;
import com.caco3.mvk.vk.audio.Audio;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.atomic.AtomicLong;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class AudioDownloadViewCompositeTest {
  private AudiosGenerator audiosGenerator = new AudiosGenerator();
  private AudioDownloadViewsComposite composite = new AudioDownloadViewsComposite();
  private Audio dummyAudio;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    dummyAudio = audiosGenerator.generateAudio();
  }

  @Test
  public void showDownloadPendingCalled_underlyingViewsShowDownloadPendingCalled() {
    final AtomicLong counter = new AtomicLong();
    final int numOfViews = 10;
    for(int i = 0; i < numOfViews; i++) {
      AudioDownloadView view = mock(AudioDownloadView.class);
      doAnswer(new Answer<Object>() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
          counter.incrementAndGet();
          return null;
        }
      }).when(view).showDownloadPending(any(Audio.class));

      composite.add(view);
    }

    composite.showDownloadPending(dummyAudio);

    assertEquals(numOfViews, counter.get());
  }

  @Test
  public void showDownloadFailed_underlyingViewsShowDownloadFailed() {
    final AtomicLong counter = new AtomicLong();
    final int numOfViews = 10;
    for(int i = 0; i < numOfViews; i++) {
      AudioDownloadView view = mock(AudioDownloadView.class);
      doAnswer(new Answer<Object>() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
          counter.incrementAndGet();
          return null;
        }
      }).when(view).showDownloadFailed(any(Audio.class));

      composite.add(view);
    }

    composite.showDownloadFailed(dummyAudio);

    assertEquals(numOfViews, counter.get());
  }

  @Test
  public void showDownloadSuccessful_underlyingViewsShowDownloadSuccessful() {
    final AtomicLong counter = new AtomicLong();
    final int numOfViews = 10;
    for(int i = 0; i < numOfViews; i++) {
      AudioDownloadView view = mock(AudioDownloadView.class);
      doAnswer(new Answer<Object>() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
          counter.incrementAndGet();
          return null;
        }
      }).when(view).showDownloadSuccessful(any(Audio.class));

      composite.add(view);
    }

    composite.showDownloadSuccessful(dummyAudio);

    assertEquals(numOfViews, counter.get());
  }

  @Test
  public void showDownloadCanceled_underlyingViewsShowDownloadCanceled() {
    final AtomicLong counter = new AtomicLong();
    final int numOfViews = 10;
    for(int i = 0; i < numOfViews; i++) {
      AudioDownloadView view = mock(AudioDownloadView.class);
      doAnswer(new Answer<Object>() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
          counter.incrementAndGet();
          return null;
        }
      }).when(view).showDownloadCanceled(any(Audio.class));

      composite.add(view);
    }

    composite.showDownloadCanceled(dummyAudio);

    assertEquals(numOfViews, counter.get());
  }

  @Test
  public void showProgressCanceled_underlyingViewsShowProgressCalled() {
    final AtomicLong counter = new AtomicLong();
    final int numOfViews = 10;
    for(int i = 0; i < numOfViews; i++) {
      AudioDownloadView view = mock(AudioDownloadView.class);
      doAnswer(new Answer<Object>() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
          counter.incrementAndGet();
          return null;
        }
      }).when(view).updateProgress(any(Audio.class), anyLong(), anyLong(), anyLong());

      composite.add(view);
    }

    composite.updateProgress(dummyAudio, 10, 10, 10);

    assertEquals(numOfViews, counter.get());
  }

  @Test
  public void viewRemoved_compositeNotContainsView() {
    AudioDownloadView view = mock(AudioDownloadView.class);
    composite.add(view);
    assertTrue(composite.views.contains(view));
    composite.remove(view);
    assertFalse(composite.views.contains(view));
  }
}
