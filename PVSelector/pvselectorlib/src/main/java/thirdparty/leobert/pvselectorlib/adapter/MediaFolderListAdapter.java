/*
 * MIT License
 *
 * Copyright (c) 2017 leobert-lan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package thirdparty.leobert.pvselectorlib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yalantis.ucrop.entity.LocalMedia;
import com.yalantis.ucrop.entity.LocalMediaFolder;

import java.util.ArrayList;
import java.util.List;

import thirdparty.leobert.pvselectorlib.R;

/**
 * adapt {@link LocalMediaFolder} data for RecyclerView,
 */
public class MediaFolderListAdapter
        extends RecyclerView.Adapter<MediaFolderListAdapter.ViewHolder> {
    private List<LocalMediaFolder> folders = new ArrayList<>();

//    public MediaFolderListAdapter() {
//        super();
//    }

    public void bindFolderData(List<LocalMediaFolder> folders) {
        this.folders = folders;
        notifyDataSetChanged();
    }

    public List<LocalMediaFolder> getFolderData() {
        return folders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_oflist_media_folder,
                        parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LocalMediaFolder folder = folders.get(position);

        holder.onBindData(folder);

        //reset onclick listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null)
                    notifyDataSetChanged();
                onItemClickListener.onItemClick(folder.getName(), folder.getImages());
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView tvFolderName;
        TextView mediaCount;
        TextView selectedCount;

        ViewHolder(View itemView) {
            super(itemView);
            imgCover = (ImageView) itemView.findViewById(R.id.item_image_cover);
            tvFolderName = (TextView) itemView.findViewById(R.id.item_tv_folder_name);
            mediaCount = (TextView) itemView.findViewById(R.id.item_tv_media_count);
            selectedCount = (TextView) itemView.findViewById(R.id.item_tv_selected_count);
        }

        public void onBindData(LocalMediaFolder mediaFolder) {
            String name = mediaFolder.getName();
            int imageNum = mediaFolder.getMediaCount();
            String imagePath = mediaFolder.getFirstImagePath();

            if (mediaFolder.isSelectedMediaContains()) {
                selectedCount.setVisibility(View.VISIBLE);
                selectedCount.setText(String.valueOf(mediaFolder.getSelectedMediaCount()));
            } else
                selectedCount.setVisibility(View.INVISIBLE);

            loadCover(imagePath,mediaFolder.getType());
            mediaCount.setText("(" + imageNum + ")");
            tvFolderName.setText(name);
        }

        private void loadCover(String path,@LocalMedia.MediaType int type) {
            if (type == LocalMedia.TYPE_VIDEO) {
                Glide.with(getContext())
                        .load(path)
                        .thumbnail(0.5f)
                        .into(imgCover);
            } else {
                Glide.with(getContext())
                        .load(path)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .centerCrop()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(imgCover);
            }
        }

        private Context getContext() {
            return itemView.getContext();
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String folderName, List<LocalMedia> images);
    }
}
