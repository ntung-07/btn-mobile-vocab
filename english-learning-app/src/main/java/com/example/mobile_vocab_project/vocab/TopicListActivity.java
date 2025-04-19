package com.example.mobile_vocab_project.vocab;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile_vocab_project.R;
import com.example.mobile_vocab_project.VocabEntity;
import java.util.ArrayList;
import java.util.Arrays;
import com.example.mobile_vocab_project.vocab.Topic;

public class TopicListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Topic> topicList;  // Đảm bảo khai báo biến này đúng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);

        recyclerView = findViewById(R.id.topicRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Danh sách chủ đề
        topicList = new ArrayList<>();

// Chủ đề 1: Động vật
        topicList.add(new Topic(getString(R.string.topic_animals), Arrays.asList(
                new VocabEntity("Cat", "Mèo", "[kæt]"),
                new VocabEntity("Dog", "Chó", "[dɔg]"),
                new VocabEntity("Elephant", "Voi", "[ˈɛlɪfənt]"),
                new VocabEntity("Tiger", "Hổ", "[ˈtaɪɡər]"),
                new VocabEntity("Monkey", "Khỉ", "[ˈmʌŋki]"),
                new VocabEntity("Snake", "Rắn", "[sneɪk]"),
                new VocabEntity("Bear", "Gấu", "[bɛr]"),
                new VocabEntity("Fox", "Cáo", "[fɑks]"),
                new VocabEntity("Wolf", "Sói", "[wʊlf]"),
                new VocabEntity("Cow", "Bò", "[kaʊ]"),
                new VocabEntity("Pig", "Lợn", "[pɪɡ]"),
                new VocabEntity("Horse", "Ngựa", "[hɔːrs]"),
                new VocabEntity("Sheep", "Cừu", "[ʃiːp]"),
                new VocabEntity("Goat", "Dê", "[ɡoʊt]"),
                new VocabEntity("Chicken", "Gà", "[ˈʧɪkən]"),
                new VocabEntity("Duck", "Vịt", "[dʌk]"),
                new VocabEntity("Rabbit", "Thỏ", "[ˈræbɪt]"),
                new VocabEntity("Frog", "Ếch", "[frɔːɡ]"),
                new VocabEntity("Deer", "Nai", "[dɪr]"),
                new VocabEntity("Giraffe", "Hươu cao cổ", "[dʒəˈræf]")
        )));

// Chủ đề 2: Thức ăn
        topicList.add(new Topic(getString(R.string.topic_food), Arrays.asList(
                new VocabEntity("Bread", "Bánh mì", "[brɛd]"),
                new VocabEntity("Rice", "Cơm", "[raɪs]"),
                new VocabEntity("Meat", "Thịt", "[miːt]"),
                new VocabEntity("Fish", "Cá", "[fɪʃ]"),
                new VocabEntity("Egg", "Trứng", "[ɛɡ]"),
                new VocabEntity("Milk", "Sữa", "[mɪlk]"),
                new VocabEntity("Cheese", "Phô mai", "[ʧiːz]"),
                new VocabEntity("Butter", "Bơ", "[ˈbʌtər]"),
                new VocabEntity("Apple", "Táo", "[ˈæpəl]"),
                new VocabEntity("Banana", "Chuối", "[bəˈnænə]"),
                new VocabEntity("Orange", "Cam", "[ˈɔːrɪndʒ]"),
                new VocabEntity("Grape", "Nho", "[ɡreɪp]"),
                new VocabEntity("Watermelon", "Dưa hấu", "[ˈwɔːtərˌmɛlən]"),
                new VocabEntity("Soup", "Súp", "[suːp]"),
                new VocabEntity("Noodle", "Mì", "[ˈnuːdl]"),
                new VocabEntity("Chicken", "Gà", "[ˈʧɪkən]"),
                new VocabEntity("Beef", "Thịt bò", "[biːf]"),
                new VocabEntity("Pork", "Thịt heo", "[pɔːrk]"),
                new VocabEntity("Vegetable", "Rau", "[ˈvɛdʒtəbl]"),
                new VocabEntity("Pizza", "Pizza", "[ˈpiːtsə]")
        )));


// Chủ đề 3: Trường học
        topicList.add(new Topic(getString(R.string.topic_school), Arrays.asList(
                new VocabEntity("Book", "Sách", "[bʊk]"),
                new VocabEntity("Pen", "Bút", "[pɛn]"),
                new VocabEntity("Pencil", "Bút chì", "[ˈpɛnsəl]"),
                new VocabEntity("Eraser", "Gôm", "[ɪˈreɪsər]"),
                new VocabEntity("Notebook", "Vở", "[ˈnoʊtbʊk]"),
                new VocabEntity("Desk", "Bàn học", "[dɛsk]"),
                new VocabEntity("Chair", "Ghế", "[ʧɛr]"),
                new VocabEntity("Student", "Học sinh", "[ˈstuːdənt]"),
                new VocabEntity("Teacher", "Giáo viên", "[ˈtiːʧər]"),
                new VocabEntity("Classroom", "Lớp học", "[ˈklæsˌruːm]"),
                new VocabEntity("Blackboard", "Bảng đen", "[ˈblækˌbɔːrd]"),
                new VocabEntity("Marker", "Bút dạ", "[ˈmɑːrkər]"),
                new VocabEntity("Ruler", "Thước", "[ˈruːlər]"),
                new VocabEntity("Scissors", "Kéo", "[ˈsɪzərz]"),
                new VocabEntity("Glue", "Keo dán", "[ɡluː]"),
                new VocabEntity("Paper", "Giấy", "[ˈpeɪpər]"),
                new VocabEntity("Exam", "Bài kiểm tra", "[ɪɡˈzæm]"),
                new VocabEntity("Homework", "Bài tập về nhà", "[ˈhoʊmwɜːrk]"),
                new VocabEntity("Library", "Thư viện", "[ˈlaɪˌbrɛri]"),
                new VocabEntity("School", "Trường học", "[skuːl]")
        )));

// Chủ đề 4: Gia đình
        topicList.add(new Topic(getString(R.string.topic_family), Arrays.asList(
                new VocabEntity("Father", "Bố", "[ˈfɑːðər]"),
                new VocabEntity("Mother", "Mẹ", "[ˈmʌðər]"),
                new VocabEntity("Brother", "Anh/em trai", "[ˈbrʌðər]"),
                new VocabEntity("Sister", "Chị/em gái", "[ˈsɪstər]"),
                new VocabEntity("Grandfather", "Ông", "[ˈɡrændˌfɑːðər]"),
                new VocabEntity("Grandmother", "Bà", "[ˈɡrændˌmʌðər]"),
                new VocabEntity("Uncle", "Chú/cậu", "[ˈʌŋkəl]"),
                new VocabEntity("Aunt", "Cô/dì", "[ænt]"),
                new VocabEntity("Cousin", "Anh/chị/em họ", "[ˈkʌzən]"),
                new VocabEntity("Son", "Con trai", "[sʌn]"),
                new VocabEntity("Daughter", "Con gái", "[ˈdɔːtər]"),
                new VocabEntity("Parent", "Phụ huynh", "[ˈpɛrənt]"),
                new VocabEntity("Child", "Con", "[ʧaɪld]"),
                new VocabEntity("Baby", "Em bé", "[ˈbeɪbi]"),
                new VocabEntity("Husband", "Chồng", "[ˈhʌzbənd]"),
                new VocabEntity("Wife", "Vợ", "[waɪf]"),
                new VocabEntity("Family", "Gia đình", "[ˈfæmɪli]"),
                new VocabEntity("Relative", "Họ hàng", "[ˈrɛlətɪv]"),
                new VocabEntity("Nephew", "Cháu trai", "[ˈnɛfjuː]"),
                new VocabEntity("Niece", "Cháu gái", "[niːs]")
        )));

        // Chủ đề 5: Màu sắc
        topicList.add(new Topic(getString(R.string.topic_colors), Arrays.asList(
                new VocabEntity("Red", "Màu đỏ", "[rɛd]"),
                new VocabEntity("Blue", "Màu xanh dương", "[bluː]"),
                new VocabEntity("Green", "Màu xanh lá", "[griːn]"),
                new VocabEntity("Yellow", "Màu vàng", "[ˈjɛloʊ]"),
                new VocabEntity("Orange", "Màu cam", "[ˈɔːrɪndʒ]"),
                new VocabEntity("Purple", "Màu tím", "[ˈpɜːrpəl]"),
                new VocabEntity("Pink", "Màu hồng", "[pɪŋk]"),
                new VocabEntity("Black", "Màu đen", "[blæk]"),
                new VocabEntity("White", "Màu trắng", "[waɪt]"),
                new VocabEntity("Gray", "Màu xám", "[ɡreɪ]"),
                new VocabEntity("Brown", "Màu nâu", "[braʊn]"),
                new VocabEntity("Cyan", "Màu xanh da trời", "[ˈsaɪ.ən]"),
                new VocabEntity("Magenta", "Màu đỏ tươi", "[məˈdʒɛn.tə]"),
                new VocabEntity("Maroon", "Màu đỏ rượu", "[məˈruːn]"),
                new VocabEntity("Beige", "Màu be", "[beɪʒ]"),
                new VocabEntity("Turquoise", "Màu ngọc lam", "[ˈtɜːrkwɔɪz]"),
                new VocabEntity("Navy", "Xanh hải quân", "[ˈneɪvi]"),
                new VocabEntity("Silver", "Màu bạc", "[ˈsɪlvər]"),
                new VocabEntity("Gold", "Màu vàng kim", "[ɡoʊld]"),
                new VocabEntity("Ivory", "Màu ngà", "[ˈaɪvəri]")
        )));

        // Chủ đề 6: Thời tiết
        topicList.add(new Topic(getString(R.string.topic_weather), Arrays.asList(
                new VocabEntity("Sunny", "Trời nắng", "[ˈsʌni]"),
                new VocabEntity("Rainy", "Trời mưa", "[ˈreɪni]"),
                new VocabEntity("Cloudy", "Trời nhiều mây", "[ˈklaʊdi]"),
                new VocabEntity("Windy", "Trời có gió", "[ˈwɪndi]"),
                new VocabEntity("Stormy", "Có bão", "[ˈstɔːrmi]"),
                new VocabEntity("Snowy", "Có tuyết", "[ˈsnoʊi]"),
                new VocabEntity("Foggy", "Có sương mù", "[ˈfɔːɡi]"),
                new VocabEntity("Hot", "Nóng", "[hɒt]"),
                new VocabEntity("Cold", "Lạnh", "[koʊld]"),
                new VocabEntity("Warm", "Ấm áp", "[wɔːrm]"),
                new VocabEntity("Cool", "Mát mẻ", "[kuːl]"),
                new VocabEntity("Humid", "Ẩm ướt", "[ˈhjuːmɪd]"),
                new VocabEntity("Dry", "Khô ráo", "[draɪ]"),
                new VocabEntity("Drizzle", "Mưa phùn", "[ˈdrɪzl]"),
                new VocabEntity("Lightning", "Sét", "[ˈlaɪtnɪŋ]"),
                new VocabEntity("Thunder", "Sấm", "[ˈθʌndər]"),
                new VocabEntity("Rainbow", "Cầu vồng", "[ˈreɪnboʊ]"),
                new VocabEntity("Snowflake", "Bông tuyết", "[ˈsnoʊfleɪk]"),
                new VocabEntity("Hail", "Mưa đá", "[heɪl]"),
                new VocabEntity("Tornado", "Lốc xoáy", "[tɔːrˈneɪdoʊ]")
        )));

        // Chủ đề 7: Giao thông
        topicList.add(new Topic(getString(R.string.topic_transportation), Arrays.asList(
                new VocabEntity("Car", "Xe hơi", "[kɑːr]"),
                new VocabEntity("Bus", "Xe buýt", "[bʌs]"),
                new VocabEntity("Bike", "Xe đạp", "[baɪk]"),
                new VocabEntity("Motorbike", "Xe máy", "[ˈmoʊtərˌbaɪk]"),
                new VocabEntity("Train", "Tàu hỏa", "[treɪn]"),
                new VocabEntity("Airplane", "Máy bay", "[ˈɛrpleɪn]"),
                new VocabEntity("Boat", "Thuyền", "[boʊt]"),
                new VocabEntity("Ship", "Tàu", "[ʃɪp]"),
                new VocabEntity("Taxi", "Xe taxi", "[ˈtæksi]"),
                new VocabEntity("Truck", "Xe tải", "[trʌk]"),
                new VocabEntity("Van", "Xe van", "[væn]"),
                new VocabEntity("Helicopter", "Trực thăng", "[ˈhɛlɪˌkɑptər]"),
                new VocabEntity("Scooter", "Xe tay ga", "[ˈskuːtər]"),
                new VocabEntity("Subway", "Tàu điện ngầm", "[ˈsʌbweɪ]"),
                new VocabEntity("Traffic", "Giao thông", "[ˈtræfɪk]"),
                new VocabEntity("Traffic light", "Đèn giao thông", "[ˈtræfɪk laɪt]"),
                new VocabEntity("Crosswalk", "Lối sang đường", "[ˈkrɔːswɔːk]"),
                new VocabEntity("Highway", "Đường cao tốc", "[ˈhaɪweɪ]"),
                new VocabEntity("Road", "Đường", "[roʊd]"),
                new VocabEntity("Bridge", "Cây cầu", "[brɪdʒ]")
        )));

        // Chủ đề 8: Đồ dùng trong nhà
        topicList.add(new Topic(getString(R.string.topic_household), Arrays.asList(
                new VocabEntity("Table", "Bàn", "[ˈteɪbl]"),
                new VocabEntity("Chair", "Ghế", "[ʧɛr]"),
                new VocabEntity("Lamp", "Đèn bàn", "[læmp]"),
                new VocabEntity("Sofa", "Ghế sofa", "[ˈsoʊfə]"),
                new VocabEntity("Bed", "Giường", "[bɛd]"),
                new VocabEntity("Pillow", "Gối", "[ˈpɪloʊ]"),
                new VocabEntity("Blanket", "Chăn", "[ˈblæŋkɪt]"),
                new VocabEntity("Curtain", "Rèm cửa", "[ˈkɜːrtn]"),
                new VocabEntity("Door", "Cánh cửa", "[dɔːr]"),
                new VocabEntity("Window", "Cửa sổ", "[ˈwɪndoʊ]"),
                new VocabEntity("Fan", "Quạt", "[fæn]"),
                new VocabEntity("Mirror", "Gương", "[ˈmɪrər]"),
                new VocabEntity("Carpet", "Thảm", "[ˈkɑːrpɪt]"),
                new VocabEntity("Closet", "Tủ đồ", "[ˈklɑːzɪt]"),
                new VocabEntity("Television", "TV", "[ˈtɛləˌvɪʒən]"),
                new VocabEntity("Refrigerator", "Tủ lạnh", "[rɪˈfrɪdʒəˌreɪtər]"),
                new VocabEntity("Microwave", "Lò vi sóng", "[ˈmaɪkrəˌweɪv]"),
                new VocabEntity("Stove", "Bếp", "[stoʊv]"),
                new VocabEntity("Sink", "Bồn rửa", "[sɪŋk]"),
                new VocabEntity("Toilet", "Bồn cầu", "[ˈtɔɪlət]")
        )));

        TopicAdapter adapter = new TopicAdapter(this, topicList);
        recyclerView.setAdapter(adapter);
    }
}
