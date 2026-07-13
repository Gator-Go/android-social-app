def changes = []

public class MyData {

  def theFile = ""

  def extMarker =
"""
"""
  def srcInsert =
"""
"""
}
MyData newData = new MyData()



//
// Post list changes
//


newData = new MyData()
newData.theFile = "AndroidManifest.xml"
newData.extMarker =
"""
        <activity android:name="CommentList"
                  android:label="Comment List">
        </activity>
"""
newData.srcInsert =
"""
        <activity android:name="CommentList"
                  android:label="Comment List">
        </activity>
        <activity android:name="PostCommentList"
                  android:label="PostComment List">
        </activity>
"""
changes.add(newData)



newData = new MyData()
newData.theFile = "MemberDao.java"
newData.extMarker =
"""
  private String orderBy = "LAST_UPDATE DESC";
"""
newData.srcInsert =
"""
  private String orderBy = "NAME ASC";
"""
changes.add(newData)



newData = new MyData()
newData.theFile = "PostingTagDao.java"
newData.extMarker =
"""
  private String orderBy = "LAST_UPDATE DESC";
"""
newData.srcInsert =
"""
  private String orderBy = "TAG ASC";
"""
changes.add(newData)



newData = new MyData()
newData.theFile = "PostingDao.java"
newData.extMarker =
"""
  private String orderBy = "LAST_UPDATE DESC";
"""
newData.srcInsert =
"""
  private String orderBy = "POSTING_DATE DESC";
"""
changes.add(newData)



newData = new MyData()
newData.theFile = "CommentDao.java"
newData.extMarker =
"""
  private String orderBy = "LAST_UPDATE DESC";
"""
newData.srcInsert =
"""
  private String orderBy = "COMMENT_DATE DESC";
"""
changes.add(newData)



newData = new MyData()
newData.theFile = "PostingAdapter.java"
newData.extMarker =
"""
           "  %s %s"
 , formatDate.format(obj.getPostingDate()), obj.getPostingTag()
"""
newData.srcInsert =
"""
           " %s %s \\n %s"
 , formatDate.format(obj.getPostingDate()), obj.getPostingTag(), obj.getPostingInfo()
"""
changes.add(newData)


newData = new MyData()
newData.theFile = "MemberList.java"
newData.extMarker =
"""
    if (doClickItem)
      longClickItem(parent, view, position, id);
"""
newData.srcInsert =
"""
    if (doClickItem) {
      doClickItem = false;
      PostingDao postingDao = new PostingDao(MemberList.this);
      Posting posting = new Posting();
      Posting posting2 = new Posting();
      posting.setMemberId(member.getId());
      ArrayList<Posting> postingValues = postingDao.searchPosting(posting, posting2);
      if (postingValues.size() == 0) {
        Toast.makeText(MemberList.this, "No Postings found", Toast.LENGTH_SHORT).show();
        return;
      }
      Intent postIntent = new Intent(MemberList.this, PostingList.class);
      Bundle postExtra = new Bundle();
      postExtra.putParcelableArrayList("Postings", postingValues);
      postIntent.putExtra("PostingsContent", postExtra);
      postIntent.putExtra("Member", member);
      startActivity(postIntent);
    }

    if (doClickItem)
      longClickItem(parent, view, position, id);
"""
changes.add(newData)


newData = new MyData()
newData.theFile = "PostingList.java"
newData.extMarker =
"""
    if (doClickItem)
      longClickItem(parent, view, position, id);
"""
newData.srcInsert =
"""
    if (doClickItem) {
      doClickItem = false;
      Intent commentIntent = new Intent(PostingList.this, PostCommentList.class);
      Bundle commentExtra = new Bundle();
      commentIntent.putExtra("Posting", posting);
      startActivity(commentIntent);
    }

    if (doClickItem)
      longClickItem(parent, view, position, id);
"""
changes.add(newData)


newData = new MyData()
newData.theFile = "CommentList.java"
newData.extMarker =
"""
    if (doClickItem)
      longClickItem(parent, view, position, id);
"""
newData.srcInsert =
"""
    if (doClickItem) {
      doClickItem = false;

      Posting myPosting = new Posting();
      PostingDao postingDao = new PostingDao(CommentList.this);
      myPosting.setId(comment.getPostingId());
      myPosting = postingDao.getPosting(myPosting);

      Intent commentIntent = new Intent(CommentList.this, PostCommentList.class);
      Bundle commentExtra = new Bundle();
      commentIntent.putExtra("Posting", myPosting);
      startActivity(commentIntent);
    }

    if (doClickItem)
      longClickItem(parent, view, position, id);
"""
changes.add(newData)


//
// End Post list changes
//


newData = new MyData()
newData.theFile = "comment_list.xml"
newData.extMarker =
"""
        android:text="  Comment Date Posting Comment"
"""
newData.srcInsert =
"""
        android:text=" Member CommentDate PostingComment"
"""
changes.add(newData)


newData = new MyData()
newData.theFile = "posting_list.xml"
newData.extMarker =
"""
        android:text="  Posting Date Posting Tag"
"""
newData.srcInsert =
"""
        android:text=" Member PostingDate PostingTag PostingInfo"
"""
changes.add(newData)



//
// Add member to post list
//


newData = new MyData()
newData.theFile = "PostingAdapter.java"
newData.extMarker =
"""
import com.swBuilder.social.app.pojos.Posting;
"""
newData.srcInsert =
"""
import com.swBuilder.social.app.pojos.Posting;
import com.swBuilder.social.app.pojos.Member;
import com.swBuilder.social.app.daos.MemberDao;
"""
changes.add(newData)


newData = new MyData()
newData.theFile = "PostingAdapter.java"
newData.extMarker =
"""
    private ImageHelper imageHelper = new ImageHelper();
"""
newData.srcInsert =
"""
    private ImageHelper imageHelper = new ImageHelper();
    private MemberDao memberDao;
    private Member member = new Member();
"""
changes.add(newData)


newData = new MyData()
newData.theFile = "PostingAdapter.java"
newData.extMarker =
"""
        this.dateTimeFormatter = new SimpleDateFormat (dateFormat + " hh:mm");
"""
newData.srcInsert =
"""
        this.dateTimeFormatter = new SimpleDateFormat (dateFormat + " hh:mm");
        memberDao = new MemberDao(context);
"""
changes.add(newData)


newData = new MyData()
newData.theFile = "PostingAdapter.java"
newData.extMarker =
"""
        Posting obj = postings.get(position);

        String result = String.format(
           " %s %s \\n %s"
 , formatDate.format(obj.getPostingDate()), obj.getPostingTag(), obj.getPostingInfo()
        );
"""
newData.srcInsert =
"""
        Posting obj = postings.get(position);

        member.setId(obj.getMemberId());
        member = memberDao.getMember(member);

        String result = String.format(
           " %s %s %s \\n %s"
 , member.getName() , formatDate.format(obj.getPostingDate()), obj.getPostingTag(), obj.getPostingInfo()
        );
"""
changes.add(newData)


//
// End add member to post list
//

//
// Remove add from comment list
//


newData = new MyData()
newData.theFile = "comment_list.xml"
newData.extMarker =
"""
        <Button
            android:id="@+id/add"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Add"
            android:drawableTop="@drawable/icon_add"
            android:textSize="15sp" 
            android:onClick="onClick"/>
"""
newData.srcInsert =
"""

"""
changes.add(newData)


newData = new MyData()
newData.theFile = "comment_menu.xml"
newData.extMarker =
"""
    <item android:id="@+id/menu_add"
          android:icon="@drawable/icon_add"
          android:title="Add"
          android:showAsAction="ifRoom" />
"""
newData.srcInsert =
"""

"""
changes.add(newData)


newData = new MyData()
newData.theFile = "CommentList.java"
newData.extMarker =
"""
    case R.id.add:
      reload = true;
      Intent addIntent = new Intent(CommentList.this, CommentAdd.class);
    if (member != null)
      addIntent.putExtra("Member", member);
    if (posting != null)
      addIntent.putExtra("Posting", posting);

      startActivity(addIntent);
      break;
"""
newData.srcInsert =
"""

"""
changes.add(newData)


newData = new MyData()
newData.theFile = "CommentList.java"
newData.extMarker =
"""
    case R.id.menu_add:
        reload = true;
        Intent addIntent = new Intent(CommentList.this, CommentAdd.class);
    if (member != null)
      addIntent.putExtra("Member", member);
    if (posting != null)
      addIntent.putExtra("Posting", posting);

        startActivity(addIntent);
        return true;
"""
newData.srcInsert =
"""

"""
changes.add(newData)


//
// End remove add from comment list
//

//
// Remember member selection
//





//
// End remember member selection
//

newData = new MyData()
newData.theFile = "PostingPostingPostVidSave.java"
newData.extMarker =
"""
    private static final String TAG = "PostingPostingPostVidSave";
"""
newData.srcInsert =
"""
    private static final String TAG = "PostingPostVidSave";
"""
changes.add(newData)

newData = new MyData()
newData.theFile = "PostingPostingPostPicSave.java"
newData.extMarker =
"""
    private static final String TAG = "PostingPostingPostPicSave";
"""
newData.srcInsert =
"""
    private static final String TAG = "PostingPostPicSave";
"""
changes.add(newData)

//
// Posting tag changes in 'post' dir (to enforce logger rules that tags can be at most 23 characters long)
//

newData = new MyData()
newData.theFile = "SyncPostingTagQueueHelper.java"
newData.extMarker =
"""
  public static final String TAG = "SyncPostingTagQueueHelper";
"""
newData.srcInsert =
"""
  public static final String TAG = "SyncPostingTagQueue";
"""
changes.add(newData)

//
// Sync file change
// 
def dir = "../social"

def extFiles ( theDir, changes ) {

   def fileList = new File(theDir).list().toList()

   for ( i in fileList ) {

      def inFile = theDir + "/" + i
      def f1= new File(inFile)

      MyData myData = new MyData();

      if ( f1.isDirectory() ) {
         extFiles ( inFile, changes )
      } else {
//println(i)
        for (c in changes) {
          MyData theData = c
          if ( i.equals(theData.theFile) ) {
            def oldFile = new File(inFile).text
            def newMarker = theData.extMarker.replaceAll( "\\\n", "\\\r\\\n" )
            def newSrc = theData.srcInsert.replaceAll( "\\\n", "\\\r\\\n" )
            def newFile = oldFile.replace(newMarker, newSrc)
            new File(inFile).write(newFile)
            if (newFile.contains(newSrc) == false) { println(theData.theFile + " missing changes") }
          }

        }

      } 
   }
}

extFiles ( dir, changes )


//add files
//
def list_comment_pic = new File("./list_comment_pic.xml").text
new File("../social/app/src/main/res/layout/list_comment_pic.xml").write(list_comment_pic)

def post_comment_list = new File("./post_comment_list.xml").text
new File("../social/app/src/main/res/layout/post_comment_list.xml").write(post_comment_list)

def PostCommentList = new File("./PostCommentList.java").text
new File("../social/app/src/main/java/com/swBuilder/social/app/PostCommentList.java").write(PostCommentList)

def PostCommentAdapter = new File("./PostCommentAdapter.java").text
new File("../social/app/src/main/java/com/swBuilder/social/app/adapters/PostCommentAdapter.java").write(PostCommentAdapter)

def CommentAdapter = new File("./CommentAdapter.java").text
new File("../social/app/src/main/java/com/swBuilder/social/app/adapters/CommentAdapter.java").write(CommentAdapter)

new File("../social/app/src/main/res/values/strings.xml").delete()

def strings = new File("./strings.xml").text
new File("../social/app/src/main/res/values/strings.xml").write(strings)
