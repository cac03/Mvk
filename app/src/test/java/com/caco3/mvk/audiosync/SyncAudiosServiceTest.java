package com.caco3.mvk.audiosync;

import android.content.Intent;

import com.caco3.mvk.BuildConfig;
import com.caco3.mvk.audiodownload.AudioDownloader;
import com.caco3.mvk.data.appuser.AppUser;
import com.caco3.mvk.data.audio.AudiosRepository;
import com.caco3.mvk.network.NetworkManager;
import com.caco3.mvk.timber.SystemOutTree;
import com.caco3.mvk.vk.Vk;
import com.caco3.mvk.vk.audio.Audio;
import com.caco3.mvk.vk.audio.AudiosGenerator;
import com.caco3.mvk.vk.audio.VkAudiosService;
import com.caco3.mvk.vk.auth.UserToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import timber.log.Timber;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = 23
)
public class SyncAudiosServiceTest {
  private SyncAudiosService service;
  @Mock private AudiosRepository repository;
  @Mock private AudioDownloader downloader;
  @Mock private Vk vk;
  @Mock private NetworkManager networkManager;
  @Mock private VkAudiosService vkAudiosService;
  @Mock private AppUser appUser;
  @Mock private UserToken userToken;
  private AudiosGenerator audiosGenerator = new AudiosGenerator();

  @Before public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    service = Robolectric.buildService(SyncAudiosService.class).get();
    service.audiosRepository = repository;
    service.audioDownloader = downloader;
    service.networkManager = networkManager;
    service.vk = vk;
    service.appUser = appUser;
    when(appUser.getUserToken()).thenReturn(userToken);
    when(vk.audios()).thenReturn(vkAudiosService);
    Timber.plant(new SystemOutTree());
  }

  @Test public void notConnectedViaWifi_noAudiosPostedToDownloader() {
    when(networkManager.isConnectedWithWiFi()).thenReturn(false);
    final List<Audio> postedToDownloader = new ArrayList<>();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        Audio audio = (Audio)invocation.getArguments()[0];
        postedToDownloader.add(audio);
        return null;
      }
    }).when(downloader).post(any(Audio.class));
    startService();
    assertThat(postedToDownloader)
            .isEmpty();
  }

  @Test public void serviceStarted_audiosFetchedFromVk() throws Exception {
    when(networkManager.isConnectedWithWiFi()).thenReturn(true);
    final AtomicBoolean getAudiosCalled = new AtomicBoolean();
    when(vkAudiosService.get()).thenAnswer(new Answer<List<Audio>>() {
      @Override
      public List<Audio> answer(InvocationOnMock invocation) throws Throwable {
        getAudiosCalled.set(true);
        return Collections.emptyList();
      }
    });
    startService();
    assertThat(getAudiosCalled.get())
            .isTrue();
  }

  @Test public void serviceStartedAndAudiosFetched_audiosSavedToRepository() throws Exception {
    when(networkManager.isConnectedWithWiFi()).thenReturn(true);
    final AtomicBoolean audiosSavedToRepository = new AtomicBoolean();
    List<Audio> newAudios = audiosGenerator.generateList(100);
    when(vkAudiosService.get()).thenReturn(newAudios);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        audiosSavedToRepository.set(true);
        return null;
      }
    }).when(repository).replaceAllByVkUserId(anyLong(), ArgumentMatchers.<Audio>anyList());
    startService();
    assertThat(audiosSavedToRepository.get())
            .isTrue();
  }

  @Test public void serviceStarted_audiosThatMustBeDownloadedArePostedToDownloader() throws Exception {
    when(networkManager.isConnectedWithWiFi()).thenReturn(true);
    List<Audio> audios = audiosGenerator.generateList(1000);
    List<Audio> expected = service.extractAudiosToDownload(audios);
    final List<Audio> actual = new ArrayList<>();
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        actual.add((Audio)invocation.getArguments()[0]);
        return null;
      }
    }).when(downloader).post(any(Audio.class));
    when(vkAudiosService.get()).thenReturn(audios);
    startService();
    assertThat(actual)
            .isEqualTo(expected);
  }

  private void startService() {
    service.onHandleIntent(new Intent(RuntimeEnvironment.application, SyncAudiosService.class));
  }
}