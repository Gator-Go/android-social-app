package com.swBuilder.social.app;

import com.swBuilder.social.app.pojos.Comment;
import com.swBuilder.social.app.daos.CommentDao;
import com.swBuilder.social.app.pojos.Member;
import com.swBuilder.social.app.daos.MemberDao;
import com.swBuilder.social.app.pojos.Posting;
import com.swBuilder.social.app.daos.PostingDao;


import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.InputType;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import android.os.Environment;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

public class PostCommentList extends Activity {
  private MemberDao memberDao;
  private CommentDao commentDao;
  private ArrayList<Comment> values = null;
  private boolean reload = false;
  private Resources res;
  private ListView listView;
  private Member member;
  private MemberString memberString = null;
  private Posting posting;
  private PostingString postingString = null;
  private ImageHelper imageHelper = new ImageHelper();


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.post_comment_list);
    res = getResources();

    memberDao = new MemberDao(this);
    commentDao = new CommentDao(this);
    member = new Member();
    memberString = new MemberString(this);
    posting = new Posting();
    postingString = new PostingString(this);


    Intent intentmemberParent = getIntent();
    member = (Member)intentmemberParent.getParcelableExtra("Member");
    Intent intentpostParent = getIntent();
    posting = (Posting)intentpostParent.getParcelableExtra("Posting");

    Comment comment1 = new Comment();
    Comment comment2 = new Comment();
    comment1.setPostingId(posting.getId());
    values = commentDao.searchComment(comment1, comment2);

    listView = (ListView) findViewById(R.id.list);
    PostCommentAdapter adapter = new PostCommentAdapter(this, values);
    listView.setAdapter(adapter);

    listView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          clickItem(parent, view, position, id);
      }
    });

    listView.setOnItemLongClickListener(new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
          longClickItem(parent, view, position, id);
          return true;
      }
    });
  }

  public void clickItem(AdapterView<?> parent, View view, int position, long id) {
    boolean doClickItem = true;
    Comment comment = values.get(position);

    if (doClickItem)
      longClickItem(parent, view, position, id);
  }

  public void longClickItem(AdapterView<?> parent, View view, int position, long id) {

   reload = true;
   Comment comment = values.get(position);
   Intent intent = new Intent(PostCommentList.this, CommentView.class);
   intent.putExtra("Comment", comment);
    if (member != null)
      intent.putExtra("Member", member);
    if (posting != null)
      intent.putExtra("Posting", posting);

   startActivity(intent);
  }

  // Will be called via the onClick attribute
  public void onClick(View view) {

    int rId = view.getId();

    if (rId == R.id.add) {
      reload = true;
      Intent addIntent = new Intent(PostCommentList.this, CommentAdd.class);
      if (member != null)
        addIntent.putExtra("Member", member);
      if (posting != null)
        addIntent.putExtra("Posting", posting);

      startActivity(addIntent);

    } else if (rId == R.id.back) {
      finish();

    }

  }

  // Initiating Menu XML file (comment_menu.xml)
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.comment_menu, menu);
      return true;
  }

  /**
   * Event Handling for Individual menu item selected
   * Identify single menu item by it's id
   * */
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
        
    int rId = item.getItemId();
    if (rId == R.id.menu_add) {
        reload = true;
        Intent addIntent = new Intent(PostCommentList.this, CommentAdd.class);
        if (member != null)
          addIntent.putExtra("Member", member);
        if (posting != null)
          addIntent.putExtra("Posting", posting);

        startActivity(addIntent);
        return true;


    } else if (rId == R.id.menu_return) {

        finish();
 
    }
    return super.onOptionsItemSelected(item);

  }  

  @Override
  protected void onResume() {

    super.onResume();
    setContentView(R.layout.post_comment_list);

    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    Boolean showButtons = sharedPrefs.getBoolean("showButtons", true);
    if (showButtons.booleanValue() == false) {
      LinearLayout buttonGroup = (LinearLayout)findViewById(R.id.buttonGroup);
      buttonGroup.removeAllViews();
    }

    if (reload) {
      boolean searchFlag = false;
      Comment comment = new Comment();
      Comment comment2 = new Comment();

      if (member != null) {
        searchFlag = true;
        comment.setMemberId(member.getId());
        comment.setMemberCloudId(member.getCloudId());
      }
      if (posting != null) {
        searchFlag = true;
        comment.setPostingId(posting.getId());
        comment.setPostingCloudId(posting.getCloudId());
      }

    }

    if (member == null) {
        member = memberDao.getMemberById(posting.getMemberId());
    }

    String postParentStr = member.getName() + " " + postingString.getDisplayString(posting);

    TextView postParent = (TextView)findViewById(R.id.postParent);
    if (posting == null) {
      postParent.setVisibility(View.INVISIBLE);
    } else {
      postParent.setText(postParentStr);
    }

    TextView postInfo = (TextView)findViewById(R.id.postInfo);
    if (posting == null) {
      postInfo.setVisibility(View.INVISIBLE);
    } else {
      postInfo.setText(posting.getPostingInfo());
    }


    ImageView memberView = (ImageView) findViewById(R.id.postMember);
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
        memberView.setImageBitmap(showThumbBitmap);
    } else {
        memberView.setImageResource(R.drawable.icon_pic);
    }


    //Get the display dimensions
    DisplayMetrics metrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(metrics);
    int heightScreen = metrics.heightPixels;
    int height = heightScreen / 4;

Log.d("###############", "height: " + height);


    ImageView imageView = (ImageView) findViewById(R.id.postPhoto);

    if (posting.getPostingType().equals("pic")) {

        String postPath = "/Posting/Posting/" + posting.getPostingName() + ".jpg";
        File postFile = new  File(sdPath + postPath);
        if(postFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(postFile.getAbsolutePath(), options);

            int originalWidth = options.outWidth;
            int originalHeight = options.outHeight;
            double ratio =  1.0 * originalWidth / originalHeight;
            int width = (int) (height * ratio);
Log.d("############### PIC ", "width: " + width);

            options.inJustDecodeBounds = false;
            options.inSampleSize = imageHelper.calcSampleSizeHeight(options.outHeight, height);
            Bitmap bitmap = BitmapFactory.decodeFile(postFile.getAbsolutePath(),options);
            Bitmap showPostBitmap = imageHelper.resizeBitmap(bitmap, width, height);
            imageView.setImageBitmap(showPostBitmap);
        } else {
            imageView.setImageResource(R.drawable.icon_pic);
        }

        imageView.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v) {
            Intent postPicturePhotoIntent = new Intent(PostCommentList.this, SocialPostPicView.class);
            String postPicturePhotoPath = "/Posting/Posting/" + posting.getPostingName() + ".jpg";
            postPicturePhotoIntent.putExtra("postPath", postPicturePhotoPath);
            startActivity(postPicturePhotoIntent);
          }
        });

    } else {

        String postPath = "/Posting/Posting/" + posting.getPostingName() + ".mp4";
        File postFile = new  File(sdPath + postPath);
        if(postFile.exists()) {
 
          MediaMetadataRetriever retriever = new MediaMetadataRetriever();
          try {

            retriever.setDataSource(sdPath + postPath);
            Bitmap bitmap = retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST);
            try {
              retriever.release();
            } catch (IOException e) {
              Log.e("############### VID ", "retriever.release:" + e.getMessage());
              e.printStackTrace();
            }

            int originalWidth = bitmap.getWidth();
            int originalHeight = bitmap.getHeight();
            double ratio =  1.0 * originalWidth / originalHeight;
            int width = (int) (height * ratio);
Log.d("############### VID ", "width: " + width);
            Bitmap showPostBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            imageView.setImageBitmap(showPostBitmap);

          } catch (IllegalArgumentException e) {
            Log.e("############### VID ", "PostCommentList fail:" + e.getMessage());
            e.printStackTrace();
          }

       } else {
            imageView.setImageResource(R.drawable.icon_pic);
       }

        imageView.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v) {
            Intent postPictureVidIntent = new Intent(PostCommentList.this, SocialPostVidView.class);
            String postPictureVidPath = "/Posting/Posting/" + posting.getPostingName() + ".mp4";
            postPictureVidIntent.putExtra("postPath", postPictureVidPath);
            startActivity(postPictureVidIntent);
          }
        });

    }

    Comment comment1 = new Comment();
    Comment comment2 = new Comment();
    comment1.setPostingId(posting.getId());
    values = commentDao.searchComment(comment1, comment2);

    listView = (ListView) findViewById(R.id.list);
    PostCommentAdapter adapter = new PostCommentAdapter(this, values);
    listView.setAdapter(adapter);

    listView.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          clickItem(parent, view, position, id);
      }
    });

    listView.setOnItemLongClickListener(new OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
          longClickItem(parent, view, position, id);
          return true;
      }
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
  }



  void helpAlert(String helpText) {

     AlertDialog.Builder helpAlert = new AlertDialog.Builder(this);
     helpAlert.setTitle(res.getString(R.string.helpText));
     helpAlert.setMessage(helpText);
     helpAlert.setCancelable(false);

     helpAlert.setPositiveButton(res.getString(R.string.ok), new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          dialog.dismiss();
        }
     });

     final AlertDialog helpAlertDialog = helpAlert.create();
     helpAlertDialog.show();
  }

  public static boolean isNumeric(String str)
  {
    for (char c : str.toCharArray())
    {
        if (!Character.isDigit(c)) return false;
    }
    return true;
  }

} 
