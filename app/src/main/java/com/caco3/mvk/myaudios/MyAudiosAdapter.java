package com.caco3.mvk.myaudios;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caco3.mvk.R;
import com.caco3.mvk.vk.audio.Audio;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.caco3.mvk.util.Preconditions.checkNotNull;

public class MyAudiosAdapter extends RecyclerView.Adapter<MyAudiosAdapter.AudioViewHolder> {

  /*package*/ interface UiEventsListener {
    void onAudioItemClicked(Audio audio, View clickedView);
  }

  private Context context;
  private UiEventsListener uiEventsListener;
  private Comparator<Audio> comparator;
  private final SortedList<Audio> items = new SortedList<>(Audio.class, new SortedList.Callback<Audio>() {
    @Override
    public int compare(Audio o1, Audio o2) {
      return comparator.compare(o1, o2);
    }

    @Override
    public void onChanged(int position, int count) {
      notifyItemChanged(position, count);
    }

    @Override
    public boolean areContentsTheSame(Audio oldItem, Audio newItem) {
      return oldItem.equals(newItem);
    }

    @Override
    public boolean areItemsTheSame(Audio item1, Audio item2) {
      return item1.getId().equals(item2.getId());
    }

    @Override
    public void onInserted(int position, int count) {
      notifyItemRangeInserted(position, count);
    }

    @Override
    public void onRemoved(int position, int count) {
      notifyItemRangeRemoved(position, count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
      notifyItemMoved(fromPosition, toPosition);
    }
  });

  /*package*/ MyAudiosAdapter(UiEventsListener listener, Comparator<Audio> comparator) {
    this.uiEventsListener = checkNotNull(listener, "listener == null");
    this.comparator = checkNotNull(comparator, "comparator == null");
  }

  public void setItems(List<Audio> items) {
    this.items.beginBatchedUpdates();
    removeAllThatAreNotIn(items);
    this.items.addAll(items);
    this.items.endBatchedUpdates();
  }

  private void removeAllThatAreNotIn(List<Audio> anotherList) {
    Collections.sort(anotherList, comparator);
    for (int i = items.size() - 1; i >= 0; i--) {
      Audio audio = items.get(i);
      if (Collections.binarySearch(anotherList, audio, comparator) < 0) {
        items.remove(audio);
      }
    }
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
    TextView downloadedView;

    public AudioViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void bind(final Audio audio) {
      artistView.setText(audio.getArtist());
      titleView.setText(audio.getTitle());
      durationView.setText(formatDuration(audio.getDurationSeconds()));
      downloadedView.setText(audio.isDownloaded() ? "Y" : "N");
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          uiEventsListener.onAudioItemClicked(audio, itemView);
        }
      });
    }

    @SuppressLint("DefaultLocale")
    private String formatDuration(int durationSeconds) {
      int minutes = durationSeconds / SECONDS_PER_MINUTE;
      int seconds = durationSeconds % SECONDS_PER_MINUTE;
      return String.format(AUDIO_DURATION_STR_FMT, minutes, seconds);
    }
  }
}
