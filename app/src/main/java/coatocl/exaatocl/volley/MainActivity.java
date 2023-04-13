package coatocl.exaatocl.volley;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    TextView textView;
    String ifscCode;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        requestQueue = Volley.newRequestQueue(this);

        button.setOnClickListener(v -> {
            ifscCode = editText.getText().toString();
            if(TextUtils.isEmpty(ifscCode))
            {
                Toast.makeText(this,"please enter valid Ifsc Code...",Toast.LENGTH_SHORT).show();
            }
            else
            {
                getDataFromIfcCode();
            }
        });
    }

    private void getDataFromIfcCode()
    {
        ifscCode = editText.getText().toString();
        requestQueue.getCache().clear();

        String  url = "http://api.techm.co.in/api/v1/ifsc/" + ifscCode;

//        RequestQueue queue = Volley.newRequestQueue(this);

        @SuppressLint("SetTextI18n")
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null,response -> {
            try {
                if (response.getString("status").equals("failed")) {
                    textView.setText("Invalid IFSc Code");
                } else {
                    JSONObject jsonObject = response.getJSONObject("data");
                    String state = jsonObject.optString("STATE");
                    String bankName = jsonObject.optString("BANK");
                    String branch = jsonObject.optString("BRANCH");
                    String address = jsonObject.optString("ADDRESS");
                    String contact = jsonObject.optString("CONTACT");
                    String micrCode = jsonObject.optString("MICRCODE");
                    String city = jsonObject.optString("CITY");

                    textView.setText("Bank Name:" + bankName + "\nBranch:" + branch + "\nAddress:" + address + "\nContact:" + contact + "\nState:" + state + "\nCity:" + city + "\nMicroCode:" + micrCode);
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
                textView.setText("Invalid Ifsc Code1");
            }
        } , error -> textView.setText("Invalid IFSc Code2"));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}