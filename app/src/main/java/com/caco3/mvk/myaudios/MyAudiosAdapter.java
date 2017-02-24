package com.caco3.mvk.myaudios;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caco3.mvk.R;
import com.caco3.mvk.util.Integers;
import com.caco3.mvk.util.Longs;
import com.caco3.mvk.vk.audio.Audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class MyAudiosAdapter extends RecyclerView.Adapter<MyAudiosAdapter.AudioViewHolder> {
  private static final Comparator<Audio> audioByIdComparator = new Comparator<Audio>() {
    @Override
    public int compare(Audio o1, Audio o2) {
      return Longs.compare(o1.getId(), o2.getId());
    }
  };
  private static final Comparator<Audio> audioByVkPlaylistPositionComparator = new Comparator<Audio>() {
    @Override
    public int compare(Audio o1, Audio o2) {
      return Integers.compare(o1.getVkPlaylistPosition(), o2.getVkPlaylistPosition());
    }
  };

  /*package*/ interface UiEventsListener {
    void onAudioItemClicked(Audio audio, View clickedView);
    void onAudioLongClick(Audio audio);
  }
  private static final int GRAY_COLOR = Color.parseColor("#CCCCCC");

  private Context context;
  private UiEventsListener uiEventsListener;
  final List<Audio> items = new ArrayList<>();
  private final Set<Audio> selected = new TreeSet<>(audioByIdComparator);

  /*package*/ MyAudiosAdapter(UiEventsListener listener) {
    this.uiEventsListener = checkNotNull(listener, "listener == null");
  }

  public void setItems(List<Audio> items) {
    this.items.clear();
    this.items.addAll(items);
    notifyDataSetChanged();
    // TODO: 2/5/17 Do it gently.. trigger animations
  }

  public void selectAudio(Audio audio) {
    selected.add(audio);
    notifyItemChanged(binarySearch(audio));
  }

  public void cancelSelect(Audio audio) {
    selected.remove(audio);
    notifyItemChanged(binarySearch(audio));
  }

  private int binarySearch(Audio audio) {
    return Collections.binarySearch(items, audio, audioByVkPlaylistPositionComparator);
  }

  @Override
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    this.context = recyclerView.getContext();
  }

  @Override
  public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    super.onDetachedFromRecyclerView(recyclerView);
    context = null;
  }

  @Override
  public AudioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View root = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.audios_item, parent, false);
    return new AudioViewHolder(root);
  }

  @Override
  public void onBindViewHolder(AudioViewHolder holder, int position) {
    holder.bind(items.get(position));
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  private static final int SECONDS_PER_MINUTE = (int)TimeUnit.MINUTES.toSeconds(1);
  private static final String AUDIO_DURATION_STR_FMT = "%d:%02d";

  /*package*/ class AudioViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.audio_item_artist)
    TextView artistView;
    @BindView(R.id.audio_item_title)
    TextView titleView;
    @BindView(R.id.audio_item_duration)
    TextView durationView;
    @BindView(R.id.audio_item_downloaded)
    ImageView downloadedView;

    public AudioViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final Audio audio) {
      artistView.setText(audio.getArtist());
      titleView.setText(audio.getTitle());
      durationView.setText(formatDuration(audio.getDurationSeconds()));
      downloadedView.setVisibility(audio.isDownloaded() ? View.VISIBLE : View.GONE);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          uiEventsListener.onAudioItemClicked(audio, itemView);
        }
      });
      itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          uiEventsListener.onAudioLongClick(audio);
          return true;
        }
      });
      if (selected.contains(audio)) {
        showAsSelected();
      } else {
        showAsNotSelected();
      }
    }

    private void showAsSelected() {
      itemView.setBackgroundColor(GRAY_COLOR);
    }

    private void showAsNotSelected() {
      itemView.setBackgroundColor(Color.WHITE);
    }

    @SuppressLint("DefaultLocale")
    private String formatDuration(int durationSeconds) {
      int minutes = durationSeconds / SECONDS_PER_MINUTE;
      int seconds = durationSeconds % SECONDS_PER_MINUTE;
      return String.format(AUDIO_DURATION_STR_FMT, minutes, seconds);
    }
  }
}
