package thirdparty.leobert.pvselectorlib.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yalantis.ucrop.dialog.OptAnimationLoader;
import com.yalantis.ucrop.entity.GridItemInfoHolder;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import thirdparty.leobert.pvselectorlib.R;
import thirdparty.leobert.pvselectorlib.model.FunctionConfig;

public class PictureImageGridAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_TYPE_CAMERA = 1;
    private static final int VIEW_TYPE_PICTURE = 2;

    private Context context;
    private boolean showCamera = true;
    private OnPhotoSelectChangedListener imageSelectChangedListener;
    private int maxSelectNum;
    private List<LocalMedia> datas = new ArrayList<>();
    private List<LocalMedia> selectedMedia = new ArrayList<>();

    private boolean enablePreviewPicture;
    private boolean enablePreviewVideo = false;

    private int selectMode = FunctionConfig.SELECT_MODE_MULTIPLE;

    @DrawableRes
    private int cbDrawable;

    /**
     * true, display the No. of the candidate
     */
    private boolean displayCandidateNo;

    public PictureImageGridAdapter(Context context,
                                   boolean showCamera,
                                   int maxSelectNum,
                                   int mode,
                                   boolean enablePreviewPicture,
                                   boolean enablePreviewVideo,
                                   int cbDrawable,
                                   boolean displayCandidateNo) {
        this.context = context;
        this.selectMode = mode;
        this.showCamera = showCamera;
        this.maxSelectNum = maxSelectNum;
        this.enablePreviewPicture = enablePreviewPicture;
        this.enablePreviewVideo = enablePreviewVideo;
        this.cbDrawable = cbDrawable;
        this.displayCandidateNo = displayCandidateNo;
    }

    public void bindMediaData(List<LocalMedia> localMedias) {
        this.datas = localMedias;
        notifyDataSetChanged();
    }


    public void bindSelectedMedias(List<LocalMedia> selectedMedias) {
        this.selectedMedia = selectedMedias;
        notifyDataSetChanged();
        rebuildSelectPosition();
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectedMedia);
        }
    }

    public List<LocalMedia> getSelectedImages() {
        return selectedMedia;
    }

    public List<LocalMedia> getDatas() {
        return datas;
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera && position == 0) {
            return VIEW_TYPE_CAMERA;
        } else {
            return VIEW_TYPE_PICTURE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CAMERA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item_camera,
                    parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_image_grid_item,
                    parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,
                                 final int position) {
        if (getItemViewType(position) == VIEW_TYPE_CAMERA) {
            onBindCameraViewHolder((HeaderViewHolder) holder);
        } else {
            final ViewHolder contentHolder = (ViewHolder) holder;

            onBindContentViewHolder(contentHolder, position);
        }
    }

    private void onBindCameraViewHolder(HeaderViewHolder headerViewHolder) {
        headerViewHolder.headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageSelectChangedListener != null) {
                    imageSelectChangedListener.onTakePhoto();
                }
            }
        });
    }

    private void onBindContentViewHolder(ViewHolder contentHolder, int position) {
        final int bias = showCamera ? -1 : 0;
        final LocalMedia image = datas.get(position + bias);

        image.getGridItemInfoHolder().setAdapterPosition(contentHolder.getAdapterPosition());

        contentHolder.reConfig(image,
                selectMode,
                enablePreviewPicture,
                enablePreviewVideo,
                position);

        if (displayCandidateNo)
            refreshCandidateNo(contentHolder, image);

        setMediaSelectState(contentHolder, isSelected(image), false);
    }


    @Override
    public int getItemCount() {
        return showCamera ? datas.size() + 1 : datas.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        View headerView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerView = itemView;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;
        TextView check;
        TextView tv_duration;
        View contentView;
        LinearLayout ll_check;
        RelativeLayout rl_duration;

        ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            picture = (ImageView) itemView.findViewById(R.id.picture);
            check = (TextView) itemView.findViewById(R.id.check);
            ll_check = (LinearLayout) itemView.findViewById(R.id.toolbar_area_selector);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            rl_duration = (RelativeLayout) itemView.findViewById(R.id.rl_duration);
        }

        public void reConfig(final LocalMedia localMedia, int selectMode,
                             boolean enablePreviewPicture,
                             boolean enablePreviewVideo,
                             int position) {

            check.setBackgroundResource(cbDrawable);
            initSelectMode(selectMode);

            if (localMedia.getType() == LocalMedia.TYPE_VIDEO)
                asVideo(localMedia.getPath(), localMedia.getDuration());
            else /*only picture as possible*/
                asPicture(localMedia.getPath());

            setupClickEvent(localMedia, enablePreviewPicture, enablePreviewVideo, position);
        }

        private void setupClickEvent(final LocalMedia localMedia,
                                     final boolean enablePreviewPicture,
                                     final boolean enablePreviewVideo,
                                     final int position) {
            if (enablePreviewPicture || enablePreviewVideo)
                ll_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeMediaSelectState(ViewHolder.this, localMedia);
                    }
                });

            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int type = localMedia.getType();
                    if (type == LocalMedia.TYPE_VIDEO
                            && (selectMode == FunctionConfig.SELECT_MODE_SINGLE || enablePreviewVideo)) {
                        /* when single select mode or video preview enabled
                        * click item to preview
                        * */

                        if (imageSelectChangedListener == null)
                            return;

                        int index = showCamera ? position - 1 : position;
                        imageSelectChangedListener.onMediaClick(localMedia, index);

                        return;
                    }

                    if (type == LocalMedia.TYPE_PICTURE
                            && (selectMode == FunctionConfig.SELECT_MODE_SINGLE || enablePreviewPicture)) {
                        /* when single select mode or video preview enabled
                        * click item to preview
                        * */

                        if (imageSelectChangedListener == null)
                            return;

                        int index = showCamera ? position - 1 : position;
                        imageSelectChangedListener.onMediaClick(localMedia, index);
                        return;
                    }

                    changeMediaSelectState(ViewHolder.this, localMedia);
                }
            });

        }


        private void initSelectMode(int selectMode) {
            final int selectHintVisibility =
                    selectMode == FunctionConfig.SELECT_MODE_SINGLE ?
                            View.GONE : View.VISIBLE;
            ll_check.setVisibility(selectHintVisibility);
        }

        private void asVideo(String path, long duration) {
            Glide.with(context).load(path).into(picture);
            rl_duration.setVisibility(View.VISIBLE);
            String _s = "时长:" + timeParse(duration);
            tv_duration.setText(_s);
        }

        private void asPicture(String path) {
            Glide.with(context)
                    .load(path)
                    .placeholder(R.drawable.image_placeholder)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .into(picture);

            rl_duration.setVisibility(View.GONE);
        }
    }

    private boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectedMedia) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 选择按钮更新
     */
    private void refreshCandidateNo(ViewHolder viewHolder, LocalMedia imageBean) {
        viewHolder.check.setText("");
        for (LocalMedia media : selectedMedia) {
            if (media.getPath().equals(imageBean.getPath())) {
                final int candidateNo = media.getGridItemInfoHolder().getCandidateNo();

                imageBean.getGridItemInfoHolder().setCandidateNo(candidateNo);
                viewHolder.check.setText(String.valueOf(candidateNo));
            }
        }
    }

    /**
     * 改变图片选中状态
     *
     * @param contentHolder
     * @param media
     */

    private void changeMediaSelectState(ViewHolder contentHolder, LocalMedia media) {
        boolean hasBeenSelected = contentHolder.check.isSelected();

        if (selectedMedia.size() >= maxSelectNum && !hasBeenSelected) {
            /*means add*/

            Toast.makeText(context,
                    context.getString(R.string.message_max_num, maxSelectNum),
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (hasBeenSelected) { /*remove one*/
            for (LocalMedia selected : selectedMedia) {
                if (selected.getPath().equals(media.getPath())) {
                    selectedMedia.remove(selected);
                    rebuildSelectPosition();
                    break;
                }
            }
        } else { /*add one*/
            selectedMedia.add(media);
            media.getGridItemInfoHolder().setCandidateNo(selectedMedia.size());
        }
        //通知点击项发生了改变
        notifyItemRangeChanged(contentHolder.getAdapterPosition(), datas.size());

        //handle UI
        setMediaSelectState(contentHolder, !hasBeenSelected, true);
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectedMedia);
        }
    }

    /**
     * 更新选择的顺序
     * 当移除时需要
     */
    private void rebuildSelectPosition() {
        if (displayCandidateNo) {
            for (int index = 0, len = selectedMedia.size(); index < len; index++) {
                LocalMedia media = selectedMedia.get(index);
                GridItemInfoHolder holder = media.getGridItemInfoHolder();
                holder.setCandidateNo(index + 1);
                notifyItemChanged(holder.getAdapterPosition());
//                media.setNum(index + 1);
//                notifyItemChanged(media.getPosition());
            }
        }
    }

    private void setMediaSelectState(ViewHolder holder, boolean isChecked, boolean isAnim) {
        holder.check.setSelected(isChecked);
        if (isChecked) {
            if (isAnim) {
                Animation animation = OptAnimationLoader.loadAnimation(context, R.anim.modal_in);
                holder.check.startAnimation(animation);
            }
            holder.picture.setColorFilter(ContextCompat.getColor(context, R.color.ucrop_image_overlay2), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.picture.setColorFilter(ContextCompat.getColor(context, R.color.ucrop_image_overlay), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public interface OnPhotoSelectChangedListener {
        void onTakePhoto();

        void onChange(List<LocalMedia> selectImages);

        void onMediaClick(LocalMedia media, int position);
    }

    public void setOnPhotoSelectChangedListener(OnPhotoSelectChangedListener imageSelectChangedListener) {
        this.imageSelectChangedListener = imageSelectChangedListener;
    }

    /**
     * 毫秒转时分秒
     *
     * @param duration
     * @return
     */
    public String timeParse(long duration) {
        String time = "";
        long minute = duration / 60000;
        long seconds = duration % 60000;
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }

}
