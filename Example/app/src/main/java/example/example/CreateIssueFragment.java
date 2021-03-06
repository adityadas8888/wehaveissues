package example.example;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by aiflab on 10/7/17.
 */

public class CreateIssueFragment extends Fragment implements View.OnClickListener{

    private Issue mIssue;
    private EditText mTitleField;
    private EditText mDescriptionField;
    private ImageButton mImageButton;
    private Button mSubmitButton;

    public CreateIssueFragment() {
        mIssue = new Issue();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_issue, container, false);

        mTitleField = (EditText)v.findViewById(R.id.titleEditText);
//        mTitleField.setText(mIssue.getName());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIssue.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDescriptionField = (EditText)v.findViewById(R.id.descriptionEditText);
//        mDescriptionField.setText(mIssue.getDescription());
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIssue.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mImageButton = (ImageButton)v.findViewById(R.id.addImageButton);
        mImageButton.setOnClickListener(this);

        mSubmitButton = (Button)v.findViewById(R.id.submitButton);
        mSubmitButton.setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addImageButton:
                addImageClick(v);
                break;
            case R.id.submitButton:
                submitIssue();
                break;
        }
    }

    public void addImageClick(View v) {
        Toast.makeText(getActivity(),"Add Image Button Clicked", Toast.LENGTH_SHORT).show();
        //TODO: Add Camera Capability Here

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    public void submitIssue() {
        if (mIssue.getName().equals(getResources().getString(R.string.initial_title))) {
            Toast.makeText(getActivity(),"Please name the issue", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (mIssue.getPic() == null) {
            Toast.makeText(getActivity(),"Please upload a picture of the issue", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (mIssue.getDescription().equals(getResources().getString(R.string.initial_description))) {
            Toast.makeText(getActivity(),"Please fill in a description", Toast.LENGTH_SHORT).show();
            return;
        }

        getActivity().setTitle("Issue List");
        ArrayList<Issue> theIssues = IssueList.get(getActivity()).getIssues();
        theIssues.add(mIssue);

        Intent i = new Intent(getActivity(), IssuePagerActivity.class);
        i.putExtra(IssueFragment.EXTRA_ISSUE_ID, mIssue.mId);
        startActivity(i);
        getActivity().finish();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data"); //<-- THis bitmap is the taken image
        mIssue.setPic(bitmap);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIssue.getPic() != null) {
            mImageButton.setImageBitmap(mIssue.getPic());
        }
    }
}
