package edu.education.classroom.customLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.util.StringTokenizer;

import edu.education.classroom.R;

public class BottomAttachmentSheet extends BottomSheetDialogFragment {

    private final int CAMERA_ACCESS_REQUEST_CODE = 100;
    private final int VIDEO_CAMERA_ACCESS_REQUEST_CODE = 101;
    private final int FILE_UPLOAD_ACCESS_REQUEST_CODE = 110;

    private LinearLayout cameraActivity;
    private LinearLayout videoActivity;
    private LinearLayout fileActivity;
    private View view;
    private BottomAttachmentListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bottom_attachment_sheet,container,false);

        cameraActivity = view.findViewById(R.id.camera);
        videoActivity = view.findViewById(R.id.video);
        fileActivity = view.findViewById(R.id.attachFile);

        cameraActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCamera =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCamera,CAMERA_ACCESS_REQUEST_CODE);
            }
        });

        videoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openVideoCamera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(openVideoCamera,VIDEO_CAMERA_ACCESS_REQUEST_CODE);
            }
        });

        fileActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openFileExplore = new Intent(Intent.ACTION_VIEW);
                startActivityForResult(openFileExplore,FILE_UPLOAD_ACCESS_REQUEST_CODE);
            }
        });
        return view;
    }

    public interface BottomAttachmentListener {
        void onCameraAttachment(Bitmap imageMap,int accessCode);

        void onVideoAttachment(Uri videoUrl, int video_camera_access_request_code);

        void onFileAttachment(String filePath, String fileExtension, int file_upload_access_request_code);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (BottomAttachmentListener) context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode)
        {
            case CAMERA_ACCESS_REQUEST_CODE:
                Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                listener.onCameraAttachment(cameraImage,CAMERA_ACCESS_REQUEST_CODE);
                dismiss();
                break;
            case VIDEO_CAMERA_ACCESS_REQUEST_CODE:
                Uri videoUrl = data.getData();
                listener.onVideoAttachment(videoUrl,VIDEO_CAMERA_ACCESS_REQUEST_CODE);
                dismiss();
                break;
            case FILE_UPLOAD_ACCESS_REQUEST_CODE:
                String filePath = data.getDataString();
                String pathsp[] = filePath.split("/");
                String fileExtension = null;
                for (int i=0;i<pathsp.length;i++)
                {
                    if (pathsp[i].equals("mp4"))
                    {
                        fileExtension = pathsp[i];
                    }
                    if (pathsp[i].equals("pdf"))
                    {
                        fileExtension = pathsp[i];
                    }
                }
                listener.onFileAttachment(filePath,fileExtension,FILE_UPLOAD_ACCESS_REQUEST_CODE);
                dismiss();
        }
        /*if (requestCode == CAMERA_ACCESS_REQUEST_CODE)
        {
            Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
            listener.onCameraAttachment(cameraImage,CAMERA_ACCESS_REQUEST_CODE);
            dismiss();
        }*/
    }
}
