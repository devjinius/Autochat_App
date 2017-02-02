package eugene.chatapplication;

import java.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mInputMessage;
    private Button mSendMessage;
    private LinearLayout mMessageLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mInputMessage = (EditText) findViewById(R.id.input_message);
        mSendMessage = (Button) findViewById(R.id.send_message);
        mMessageLog = (LinearLayout) findViewById(R.id.message_log);
        mSendMessage.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    //SEND 버튼을 눌렀을때
    public void onClick(View v) {
        if (v.equals(mSendMessage)) {
            String inputText = mInputMessage.getText().toString(); // 입력받은 메세지 변수에 저장


            //사용자의 입력 누적하여 로그에 저장
            TextView userMessage = new TextView(this);
            int messageColor = getResources().getColor(R.color.message_color);
            userMessage.setTextColor(messageColor);
            //XML에서 지정한 drawable 을 설정
            userMessage.setBackgroundResource(R.drawable.user_message);
            userMessage.setText(inputText);
            // 유저의 입력 오른쪽 정렬
            userMessage.setGravity(Gravity.END);
            //메세지 백그라운드의 크기를 맞춘다.
            LinearLayout.LayoutParams userMessageLayout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            userMessageLayout.gravity = Gravity.END;
            // xml에서 설정한 margin을 불러온다.
            final int marginSize = getResources().getDimensionPixelSize(R.dimen.message_margin);
            userMessageLayout.setMargins(0, marginSize, 0, marginSize); // 설정
            // 로그 맨 위에 출력
            mMessageLog.addView(userMessage, 0, userMessageLayout);

            //cpu의 조건에 따른 답변
            String answer;
            if (inputText.contains("안녕") || inputText.contains("hi")){
                answer = "안녕하세요.";
            } else if (inputText.contains("피곤")) { //피곤이 포함됐을때 답변
                answer = "고생하셨어요.";
            } else if (inputText.contains("운세")) { // 운세가 포함되면 5가지 중에 한가지로 답변
                String[] luck = {"대박", "행운", "보통", "나쁨", "몹시 나쁨"}; // 확률이 같다 나중에 확률수정
                int randomIndex = (int) (Math.random() * 5);
                answer = "당신의 운세는 " + luck[randomIndex] + "입니다.";
            } else if (inputText.contains("시간")) { // 시간이 포함되면 현재 시각을 보여준다.
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);
                int second = cal.get(Calendar.SECOND);
                answer = String.format("현재 시각은 %d시 %d분 %d초입니다.", hour, minute, second);
            } else {
                answer = "그거 괜찮네요";
            }
            final TextView cpuMessage = new TextView(this); //send 누를 때 마다 누적하여 생성
            cpuMessage.setTextColor(messageColor);
            //XML에서 지정한 drawable 을 설정
            cpuMessage.setBackgroundResource(R.drawable.cpu_message);
            cpuMessage.setText(answer); //send 누를 때 마다 누적하여 생성
            cpuMessage.setGravity(Gravity.START); // cpuMessage 왼쪽 정렬

            // input 초기화
            mInputMessage.setText("");

            // animation 추가
            TranslateAnimation userMessageAnimation =
                    new TranslateAnimation(mMessageLog.getWidth(), 0, 0, 0);
            userMessageAnimation.setDuration(1000);

            userMessageAnimation.setAnimationListener(new Animation.AnimationListener(){
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    LinearLayout.LayoutParams cpuMessageLayout = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    cpuMessageLayout.gravity = Gravity.START;
                    cpuMessageLayout.setMargins(marginSize, marginSize, marginSize, marginSize);
                    mMessageLog.addView(cpuMessage, 0, cpuMessageLayout);
                    TranslateAnimation cpuMessageAnimation =
                            new TranslateAnimation(-mMessageLog.getWidth(), 0, 0, 0);
                    cpuMessageAnimation.setDuration(1000);
                    cpuMessage.startAnimation(cpuMessageAnimation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            userMessage.startAnimation(userMessageAnimation);
        }
    }
}
