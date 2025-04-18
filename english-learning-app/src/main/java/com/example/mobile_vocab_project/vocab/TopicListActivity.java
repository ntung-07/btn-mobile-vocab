package com.example.vocab;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

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
                new Vocab("Cat", "Mèo", "[kæt]"),
                new Vocab("Dog", "Chó", "[dɔg]"),
                new Vocab("Elephant", "Voi", "[ˈɛlɪfənt]"),
                new Vocab("Tiger", "Hổ", "[ˈtaɪɡər]"),
                new Vocab("Monkey", "Khỉ", "[ˈmʌŋki]"),
                new Vocab("Snake", "Rắn", "[sneɪk]"),
                new Vocab("Bear", "Gấu", "[bɛr]"),
                new Vocab("Fox", "Cáo", "[fɑks]"),
                new Vocab("Wolf", "Sói", "[wʊlf]"),
                new Vocab("Cow", "Bò", "[kaʊ]"),
                new Vocab("Pig", "Lợn", "[pɪɡ]"),
                new Vocab("Horse", "Ngựa", "[hɔːrs]"),
                new Vocab("Sheep", "Cừu", "[ʃiːp]"),
                new Vocab("Goat", "Dê", "[ɡoʊt]"),
                new Vocab("Chicken", "Gà", "[ˈʧɪkən]"),
                new Vocab("Duck", "Vịt", "[dʌk]"),
                new Vocab("Rabbit", "Thỏ", "[ˈræbɪt]"),
                new Vocab("Frog", "Ếch", "[frɔːɡ]"),
                new Vocab("Deer", "Nai", "[dɪr]"),
                new Vocab("Giraffe", "Hươu cao cổ", "[dʒəˈræf]")
        )));

// Chủ đề 2: Thức ăn
        topicList.add(new Topic(getString(R.string.topic_food), Arrays.asList(
                new Vocab("Bread", "Bánh mì", "[brɛd]"),
                new Vocab("Rice", "Cơm", "[raɪs]"),
                new Vocab("Meat", "Thịt", "[miːt]"),
                new Vocab("Fish", "Cá", "[fɪʃ]"),
                new Vocab("Egg", "Trứng", "[ɛɡ]"),
                new Vocab("Milk", "Sữa", "[mɪlk]"),
                new Vocab("Cheese", "Phô mai", "[ʧiːz]"),
                new Vocab("Butter", "Bơ", "[ˈbʌtər]"),
                new Vocab("Apple", "Táo", "[ˈæpəl]"),
                new Vocab("Banana", "Chuối", "[bəˈnænə]"),
                new Vocab("Orange", "Cam", "[ˈɔːrɪndʒ]"),
                new Vocab("Grape", "Nho", "[ɡreɪp]"),
                new Vocab("Watermelon", "Dưa hấu", "[ˈwɔːtərˌmɛlən]"),
                new Vocab("Soup", "Súp", "[suːp]"),
                new Vocab("Noodle", "Mì", "[ˈnuːdl]"),
                new Vocab("Chicken", "Gà", "[ˈʧɪkən]"),
                new Vocab("Beef", "Thịt bò", "[biːf]"),
                new Vocab("Pork", "Thịt heo", "[pɔːrk]"),
                new Vocab("Vegetable", "Rau", "[ˈvɛdʒtəbl]"),
                new Vocab("Pizza", "Pizza", "[ˈpiːtsə]")
        )));


// Chủ đề 3: Trường học
        topicList.add(new Topic(getString(R.string.topic_school), Arrays.asList(
                new Vocab("Book", "Sách", "[bʊk]"),
                new Vocab("Pen", "Bút", "[pɛn]"),
                new Vocab("Pencil", "Bút chì", "[ˈpɛnsəl]"),
                new Vocab("Eraser", "Gôm", "[ɪˈreɪsər]"),
                new Vocab("Notebook", "Vở", "[ˈnoʊtbʊk]"),
                new Vocab("Desk", "Bàn học", "[dɛsk]"),
                new Vocab("Chair", "Ghế", "[ʧɛr]"),
                new Vocab("Student", "Học sinh", "[ˈstuːdənt]"),
                new Vocab("Teacher", "Giáo viên", "[ˈtiːʧər]"),
                new Vocab("Classroom", "Lớp học", "[ˈklæsˌruːm]"),
                new Vocab("Blackboard", "Bảng đen", "[ˈblækˌbɔːrd]"),
                new Vocab("Marker", "Bút dạ", "[ˈmɑːrkər]"),
                new Vocab("Ruler", "Thước", "[ˈruːlər]"),
                new Vocab("Scissors", "Kéo", "[ˈsɪzərz]"),
                new Vocab("Glue", "Keo dán", "[ɡluː]"),
                new Vocab("Paper", "Giấy", "[ˈpeɪpər]"),
                new Vocab("Exam", "Bài kiểm tra", "[ɪɡˈzæm]"),
                new Vocab("Homework", "Bài tập về nhà", "[ˈhoʊmwɜːrk]"),
                new Vocab("Library", "Thư viện", "[ˈlaɪˌbrɛri]"),
                new Vocab("School", "Trường học", "[skuːl]")
        )));

// Chủ đề 4: Gia đình
        topicList.add(new Topic(getString(R.string.topic_family), Arrays.asList(
                new Vocab("Father", "Bố", "[ˈfɑːðər]"),
                new Vocab("Mother", "Mẹ", "[ˈmʌðər]"),
                new Vocab("Brother", "Anh/em trai", "[ˈbrʌðər]"),
                new Vocab("Sister", "Chị/em gái", "[ˈsɪstər]"),
                new Vocab("Grandfather", "Ông", "[ˈɡrændˌfɑːðər]"),
                new Vocab("Grandmother", "Bà", "[ˈɡrændˌmʌðər]"),
                new Vocab("Uncle", "Chú/cậu", "[ˈʌŋkəl]"),
                new Vocab("Aunt", "Cô/dì", "[ænt]"),
                new Vocab("Cousin", "Anh/chị/em họ", "[ˈkʌzən]"),
                new Vocab("Son", "Con trai", "[sʌn]"),
                new Vocab("Daughter", "Con gái", "[ˈdɔːtər]"),
                new Vocab("Parent", "Phụ huynh", "[ˈpɛrənt]"),
                new Vocab("Child", "Con", "[ʧaɪld]"),
                new Vocab("Baby", "Em bé", "[ˈbeɪbi]"),
                new Vocab("Husband", "Chồng", "[ˈhʌzbənd]"),
                new Vocab("Wife", "Vợ", "[waɪf]"),
                new Vocab("Family", "Gia đình", "[ˈfæmɪli]"),
                new Vocab("Relative", "Họ hàng", "[ˈrɛlətɪv]"),
                new Vocab("Nephew", "Cháu trai", "[ˈnɛfjuː]"),
                new Vocab("Niece", "Cháu gái", "[niːs]")
        )));

        // Chủ đề 5: Màu sắc
        topicList.add(new Topic(getString(R.string.topic_colors), Arrays.asList(
                new Vocab("Red", "Màu đỏ", "[rɛd]"),
                new Vocab("Blue", "Màu xanh dương", "[bluː]"),
                new Vocab("Green", "Màu xanh lá", "[griːn]"),
                new Vocab("Yellow", "Màu vàng", "[ˈjɛloʊ]"),
                new Vocab("Orange", "Màu cam", "[ˈɔːrɪndʒ]"),
                new Vocab("Purple", "Màu tím", "[ˈpɜːrpəl]"),
                new Vocab("Pink", "Màu hồng", "[pɪŋk]"),
                new Vocab("Black", "Màu đen", "[blæk]"),
                new Vocab("White", "Màu trắng", "[waɪt]"),
                new Vocab("Gray", "Màu xám", "[ɡreɪ]"),
                new Vocab("Brown", "Màu nâu", "[braʊn]"),
                new Vocab("Cyan", "Màu xanh da trời", "[ˈsaɪ.ən]"),
                new Vocab("Magenta", "Màu đỏ tươi", "[məˈdʒɛn.tə]"),
                new Vocab("Maroon", "Màu đỏ rượu", "[məˈruːn]"),
                new Vocab("Beige", "Màu be", "[beɪʒ]"),
                new Vocab("Turquoise", "Màu ngọc lam", "[ˈtɜːrkwɔɪz]"),
                new Vocab("Navy", "Xanh hải quân", "[ˈneɪvi]"),
                new Vocab("Silver", "Màu bạc", "[ˈsɪlvər]"),
                new Vocab("Gold", "Màu vàng kim", "[ɡoʊld]"),
                new Vocab("Ivory", "Màu ngà", "[ˈaɪvəri]")
        )));

        // Chủ đề 6: Thời tiết
        topicList.add(new Topic(getString(R.string.topic_weather), Arrays.asList(
                new Vocab("Sunny", "Trời nắng", "[ˈsʌni]"),
                new Vocab("Rainy", "Trời mưa", "[ˈreɪni]"),
                new Vocab("Cloudy", "Trời nhiều mây", "[ˈklaʊdi]"),
                new Vocab("Windy", "Trời có gió", "[ˈwɪndi]"),
                new Vocab("Stormy", "Có bão", "[ˈstɔːrmi]"),
                new Vocab("Snowy", "Có tuyết", "[ˈsnoʊi]"),
                new Vocab("Foggy", "Có sương mù", "[ˈfɔːɡi]"),
                new Vocab("Hot", "Nóng", "[hɒt]"),
                new Vocab("Cold", "Lạnh", "[koʊld]"),
                new Vocab("Warm", "Ấm áp", "[wɔːrm]"),
                new Vocab("Cool", "Mát mẻ", "[kuːl]"),
                new Vocab("Humid", "Ẩm ướt", "[ˈhjuːmɪd]"),
                new Vocab("Dry", "Khô ráo", "[draɪ]"),
                new Vocab("Drizzle", "Mưa phùn", "[ˈdrɪzl]"),
                new Vocab("Lightning", "Sét", "[ˈlaɪtnɪŋ]"),
                new Vocab("Thunder", "Sấm", "[ˈθʌndər]"),
                new Vocab("Rainbow", "Cầu vồng", "[ˈreɪnboʊ]"),
                new Vocab("Snowflake", "Bông tuyết", "[ˈsnoʊfleɪk]"),
                new Vocab("Hail", "Mưa đá", "[heɪl]"),
                new Vocab("Tornado", "Lốc xoáy", "[tɔːrˈneɪdoʊ]")
        )));

        // Chủ đề 7: Giao thông
        topicList.add(new Topic(getString(R.string.topic_transportation), Arrays.asList(
                new Vocab("Car", "Xe hơi", "[kɑːr]"),
                new Vocab("Bus", "Xe buýt", "[bʌs]"),
                new Vocab("Bike", "Xe đạp", "[baɪk]"),
                new Vocab("Motorbike", "Xe máy", "[ˈmoʊtərˌbaɪk]"),
                new Vocab("Train", "Tàu hỏa", "[treɪn]"),
                new Vocab("Airplane", "Máy bay", "[ˈɛrpleɪn]"),
                new Vocab("Boat", "Thuyền", "[boʊt]"),
                new Vocab("Ship", "Tàu", "[ʃɪp]"),
                new Vocab("Taxi", "Xe taxi", "[ˈtæksi]"),
                new Vocab("Truck", "Xe tải", "[trʌk]"),
                new Vocab("Van", "Xe van", "[væn]"),
                new Vocab("Helicopter", "Trực thăng", "[ˈhɛlɪˌkɑptər]"),
                new Vocab("Scooter", "Xe tay ga", "[ˈskuːtər]"),
                new Vocab("Subway", "Tàu điện ngầm", "[ˈsʌbweɪ]"),
                new Vocab("Traffic", "Giao thông", "[ˈtræfɪk]"),
                new Vocab("Traffic light", "Đèn giao thông", "[ˈtræfɪk laɪt]"),
                new Vocab("Crosswalk", "Lối sang đường", "[ˈkrɔːswɔːk]"),
                new Vocab("Highway", "Đường cao tốc", "[ˈhaɪweɪ]"),
                new Vocab("Road", "Đường", "[roʊd]"),
                new Vocab("Bridge", "Cây cầu", "[brɪdʒ]")
        )));

        // Chủ đề 8: Đồ dùng trong nhà
        topicList.add(new Topic(getString(R.string.topic_household), Arrays.asList(
                new Vocab("Table", "Bàn", "[ˈteɪbl]"),
                new Vocab("Chair", "Ghế", "[ʧɛr]"),
                new Vocab("Lamp", "Đèn bàn", "[læmp]"),
                new Vocab("Sofa", "Ghế sofa", "[ˈsoʊfə]"),
                new Vocab("Bed", "Giường", "[bɛd]"),
                new Vocab("Pillow", "Gối", "[ˈpɪloʊ]"),
                new Vocab("Blanket", "Chăn", "[ˈblæŋkɪt]"),
                new Vocab("Curtain", "Rèm cửa", "[ˈkɜːrtn]"),
                new Vocab("Door", "Cánh cửa", "[dɔːr]"),
                new Vocab("Window", "Cửa sổ", "[ˈwɪndoʊ]"),
                new Vocab("Fan", "Quạt", "[fæn]"),
                new Vocab("Mirror", "Gương", "[ˈmɪrər]"),
                new Vocab("Carpet", "Thảm", "[ˈkɑːrpɪt]"),
                new Vocab("Closet", "Tủ đồ", "[ˈklɑːzɪt]"),
                new Vocab("Television", "TV", "[ˈtɛləˌvɪʒən]"),
                new Vocab("Refrigerator", "Tủ lạnh", "[rɪˈfrɪdʒəˌreɪtər]"),
                new Vocab("Microwave", "Lò vi sóng", "[ˈmaɪkrəˌweɪv]"),
                new Vocab("Stove", "Bếp", "[stoʊv]"),
                new Vocab("Sink", "Bồn rửa", "[sɪŋk]"),
                new Vocab("Toilet", "Bồn cầu", "[ˈtɔɪlət]")
        )));


        TopicAdapter adapter = new TopicAdapter(this, topicList);
        recyclerView.setAdapter(adapter);
    }
}
