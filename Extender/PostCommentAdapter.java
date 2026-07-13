package com.swBuilder.social.app;

import com.swBuilder.social.app.pojos.Comment;
import com.swBuilder.social.app.pojos.Member;
import com.swBuilder.social.app.daos.MemberDao;

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

public class PostCommentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Comment> comments;
    private LayoutInflater inflater;
    private SharedPreferences sharedPrefs;
    private String dateFormat;
    private SimpleDateFormat formatDate;
    private SimpleDateFormat dateTimeFormatter;
    private MemberDao memberDao;
    private Member member = new Member();
    private ImageHelper imageHelper = new ImageHelper();

    PostCommentAdapter() {
        comments = null;
    }

    public PostCommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
        this.inflater = LayoutInflater.from(context);
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.dateFormat = sharedPrefs.getString("prefDateFormat", "yyyy/MM/dd");
        this.formatDate = new SimpleDateFormat (dateFormat);
        this.dateTimeFormatter = new SimpleDateFormat (dateFormat + " hh:mm");
        memberDao = new MemberDao(context);
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
        row = inflater.inflate(R.layout.list_comment_pic, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.CommentItem);
        TextView commentView = (TextView) row.findViewById(R.id.CommentInfo);
        ImageView imageView = (ImageView) row.findViewById(R.id.MemberPic);

        Comment obj = comments.get(position);

        member.setId(obj.getMemberId());
        member = memberDao.getMember(member);

        String result = String.format(
           " %s %s",
           member.getName(), formatDate.format(obj.getCommentDate())
        );
        textView.setText(result);

        commentView.setText(obj.getPostingComment());

        if(member.getPictureVersion().intValue() > 0) {
            File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            String sdPath = sdDir.getPath();
            String thumbnailPath = "/Member/Picture/" + member.getPictureName() + ".jpg";
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


        return (row);
    }
}
