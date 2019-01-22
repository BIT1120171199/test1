package com.bytedance.android.lesson.restapi.solution;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bytedance.android.lesson.restapi.solution.Solution2C1Activity.MyViewHolder;
import com.bytedance.android.lesson.restapi.solution.bean.Feed;
import com.bytedance.android.lesson.restapi.solution.bean.FeedResponse;
import com.bytedance.android.lesson.restapi.solution.bean.PostVideoResponse;
import com.bytedance.android.lesson.restapi.solution.newtork.IMiniDouyinService;
import com.bytedance.android.lesson.restapi.solution.newtork.RetrofitManager;
import com.bytedance.android.lesson.restapi.solution.utils.ResourceUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Solution2C2Activity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final String TAG = "Solution2C2Activity";
    public Button mBtn;
    private Button mBtnRefresh;
    private List<Feed> mFeeds = new ArrayList();
    private RecyclerView mRv;
    public Uri mSelectedImage;
    private Uri mSelectedVideo;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_solution2_c2);
        initRecyclerView();
        initBtns();
    }
    private void initBtns() {
        this.mBtn = (Button) findViewById(R.id.btn);
        this.mBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String s = Solution2C2Activity.this.mBtn.getText().toString();
                if (Solution2C2Activity.this.getString(R.string.select_an_image).equals(s)) {
                    Solution2C2Activity.this.chooseImage();
                } else if (Solution2C2Activity.this.getString(R.string.select_a_video).equals(s)) {
                    Solution2C2Activity.this.chooseVideo();
                } else if (Solution2C2Activity.this.getString(R.string.post_it).equals(s)) {
                    if (Solution2C2Activity.this.mSelectedVideo == null || Solution2C2Activity.this.mSelectedImage == null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("error data uri, mSelectedVideo = ");
                        stringBuilder.append(Solution2C2Activity.this.mSelectedVideo);
                        stringBuilder.append(", mSelectedImage = ");
                        stringBuilder.append(Solution2C2Activity.this.mSelectedImage);
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                    Solution2C2Activity.this.postVideo();
                } else if (Solution2C2Activity.this.getString(R.string.success_try_refresh).equals(s)) {
                    Solution2C2Activity.this.mBtn.setText(R.string.select_an_image);
                }
            }
        });
        this.mBtnRefresh = (Button) findViewById(R.id.btn_refresh);
    }

    private void initRecyclerView() {
        this.mRv = (RecyclerView) findViewById(R.id.rv);
        this.mRv.setLayoutManager(new LinearLayoutManager(this));
        this.mRv.setAdapter(new Adapter() {
            @NonNull
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                ImageView imageView = new ImageView(viewGroup.getContext());
                imageView.setLayoutParams(new LayoutParams(-1, -2));
                imageView.setAdjustViewBounds(true);
                return new MyViewHolder(imageView);
            }

            public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
                // TODO-C2 (10) Uncomment these 2 lines, assign image url of Feed to this url variable
//                String url = mFeeds.get(i).;
//                Glide.with(iv.getContext()).load(url).into(iv);
                ImageView iv = (ImageView) viewHolder.itemView;
                Glide.with(iv.getContext()).load(((Feed) Solution2C2Activity.this.mFeeds.get(i)).getImageUrl()).into(iv);
            }

            public int getItemCount() {
                return Solution2C2Activity.this.mFeeds.size();
            }
        });
    }

    public void chooseImage() {
        // TODO-C2 (4) Start Activity to select an image
        Intent intent = new Intent();
        intent.setType("image:");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public void chooseVideo() {
        // TODO-C2 (5) Start Activity to select a video
        Intent intent = new Intent();
        intent.setType("video:");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select Video"), 2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onActivityResult() called with: requestCode = [");
        stringBuilder.append(requestCode);
        stringBuilder.append("], resultCode = [");
        stringBuilder.append(resultCode);
        stringBuilder.append("], data = [");
        stringBuilder.append(data);
        stringBuilder.append("]");
        Log.d(str, stringBuilder.toString());
        if (resultCode == -1 && data != null) {
            if (requestCode == 1) {
                this.mSelectedImage = data.getData();
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("selectedImage = ");
                stringBuilder.append(this.mSelectedImage);
                Log.d(str, stringBuilder.toString());
                this.mBtn.setText(R.string.select_a_video);
            } else if (requestCode == 2) {
                this.mSelectedVideo = data.getData();
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("mSelectedVideo = ");
                stringBuilder.append(this.mSelectedVideo);
                Log.d(str, stringBuilder.toString());
                this.mBtn.setText(R.string.post_it);
            }
        }
    }
    private Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(this, uri));
        return Part.createFormData(name, f.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), f));
    }
    private void postVideo() {
        this.mBtn.setText("POSTING...");
        this.mBtn.setEnabled(false);
        // TODO-C2 (6) Send Request to post a video with its cover image
        // if success, make a text Toast and show
        ((IMiniDouyinService) RetrofitManager.get(IMiniDouyinService.HOST).create(IMiniDouyinService.class)).createVideo("", "", getMultipartFromUri("cover_image", this.mSelectedImage), getMultipartFromUri("video", this.mSelectedVideo)).enqueue(new Callback<PostVideoResponse>() {
            public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                String str = Solution2C2Activity.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("call = [");
                stringBuilder.append(call);
                stringBuilder.append("], response = [");
                stringBuilder.append(response.body());
                stringBuilder.append("]");
                Log.d(str, stringBuilder.toString());
                if (response.isSuccessful()) {
                    str = "Post Succeed";
                    Solution2C2Activity.this.mBtn.setText(R.string.success_try_refresh);
                } else {
                    str = Solution2C2Activity.TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("response.errorBody() = [");
                    stringBuilder.append(response.errorBody());
                    stringBuilder.append("]");
                    Log.d(str, stringBuilder.toString());
                    str = "Post Failed";
                    Solution2C2Activity.this.mBtn.setText(R.string.post_it);
                }
                Toast.makeText(Solution2C2Activity.this, str, 1).show();
                Solution2C2Activity.this.mBtn.setEnabled(true);
            }
            public void onFailure(Call<PostVideoResponse> call, Throwable t) {
                String str = Solution2C2Activity.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onFailure() called with: call = [");
                stringBuilder.append(call);
                stringBuilder.append("], t = [");
                stringBuilder.append(t);
                stringBuilder.append("]");
                Log.d(str, stringBuilder.toString());
                Toast.makeText(Solution2C2Activity.this, t.getMessage(), 1).show();
                Solution2C2Activity.this.mBtn.setText(R.string.post_it);
                Solution2C2Activity.this.mBtn.setEnabled(true);
            }
        });
    }
    public void fetchFeed(View view) {
        this.mBtnRefresh.setText("requesting...");
        this.mBtnRefresh.setEnabled(false);
        // TODO-C2 (9) Send Request to fetch feed
        // if success, assign data to mFeeds and call mRv.getAdapter().notifyDataSetChanged()
        // don't forget to call resetRefreshBtn() after response received
        ((IMiniDouyinService) RetrofitManager.get(IMiniDouyinService.HOST).create(IMiniDouyinService.class)).fetchFeed().enqueue(new Callback<FeedResponse>() {
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                String str = Solution2C2Activity.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("call= ");
                stringBuilder.append(call);
                stringBuilder.append("response= ");
                stringBuilder.append(response.body());
                Log.d(str, stringBuilder.toString());
                if (response.isSuccessful()) {
                    Solution2C2Activity.this.mFeeds = ((FeedResponse) response.body()).getFeeds();
                    Solution2C2Activity.this.mRv.getAdapter().notifyDataSetChanged();
                } else {
                    str = Solution2C2Activity.TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("response.errorBody()= ");
                    stringBuilder.append(response.errorBody());
                    Log.d(str, stringBuilder.toString());
                    Toast.makeText(Solution2C2Activity.this, "fetch feed failed", 1).show();
                }
                resetBtn();
            }
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                String str = Solution2C2Activity.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("call=");
                stringBuilder.append(call);
                stringBuilder.append(", t =");
                stringBuilder.append(t);
                Log.d(str, stringBuilder.toString());
                Toast.makeText(Solution2C2Activity.this, t.getMessage(), 1).show();
                resetBtn();
            }
            private void resetBtn() {
                Solution2C2Activity.this.mBtnRefresh.setText(R.string.refresh_feed);
                Solution2C2Activity.this.mBtnRefresh.setEnabled(true);
            }
        });
    }
}