package com.swBuilder.social.app;

import com.swBuilder.social.app.pojos.Comment;
import com.swBuilder.social.app.pojos.Member;
import com.swBuilder.social.app.daos.MemberDao;
import com.swBuilder.social.app.pojos.Posting;
import com.swBuilder.social.app.daos.PostingDao;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager; 

import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

import java.io.File;
import android.os.Environment;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

public class CommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Comment> comments;
    private LayoutInflater inflater;
    private SharedPreferences sharedPrefs;
    private String dateFormat;
    private SimpleDateFormat formatDate;
    private SimpleDateFormat dateTimeFormatter;
    private ImageHelper imageHelper = new ImageHelper();
    private MemberDao memberDao;
    private Member member = new Member();
    private PostingDao postingDao;
    private Posting posting = new Posting();

    CommentAdapter() {
        comments = null;
    }

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
        this.inflater = LayoutInflater.from(context);
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.dateFormat = sharedPrefs.getString("prefDateFormat", "yyyy/MM/dd");
        this.formatDate = new SimpleDateFormat (dateFormat);
        this.dateTimeFormatter = new SimpleDateFormat (dateFormat + " hh:mm");
        memberDao = new MemberDao(context);
        postingDao = new PostingDao(context);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return comments.size();
    }

    public Comment getItem(int position) {
        return comments.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        boolean showPic = false;
        View row;
        row = inflater.inflate(R.layout.list_item_pic, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.ItemText);
        ImageView imageView = (ImageView) row.findViewById(R.id.ItemPic);

        Comment obj = comments.get(position);

        member.setId(obj.getMemberId());
        member = memberDao.getMember(member);

        posting.setId(obj.getPostingId());
        posting = postingDao.getPosting(posting);

        String result = String.format(
           " %s %s \n %s"
 , member.getName(), formatDate.format(obj.getCommentDate()), obj.getPostingComment()
        );
        textView.setText(result);


        showPic = true;
        if(posting.getPostingVersion().intValue() > 0) {
            File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String sdPath = sdDir.getPath();
            String thumbnailPath;
            if (posting.getPostingType().equals("pic"))
              thumbnailPath = "/Posting/Posting/" + posting.getCloudId() + "PicThumb" + posting.getPostingVersion().toString() + ".jpg";
            else
              thumbnailPath = "/Posting/Posting/" + posting.getCloudId() + "VidThumb" + posting.getPostingVersion().toString() + ".jpg";
            File thumbnailFile = new  File(sdPath + thumbnailPath);
            if(thumbnailFile.exists()) {
                Bitmap thumbnailBitmap = BitmapFactory.decodeFile(thumbnailFile.getAbsolutePath());
                int originalWidth = thumbnailBitmap.getWidth();
                int originalHeight = thumbnailBitmap.getHeight();
                double ratio =  1.0 * originalWidth / originalHeight;
                int height = 200;
                int width = (int) (height * ratio);
                Bitmap showThumbBitmap = imageHelper.resizeBitmap(thumbnailBitmap, width, height);
                imageView.setImageBitmap(showThumbBitmap);
            } else {
                imageView.setImageResource(R.drawable.icon_pic);
            }
        } else
            imageView.setImageResource(R.drawable.icon_pic);

        if (showPic == false)
          imageView .setVisibility(View.GONE);

        return (row);
    }
}
