package edu.education.classroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import edu.education.classroom.Classes.AnnouncementDetails;
import edu.education.classroom.R;

public class InflateAnnouncementLister extends RecyclerView.Adapter<InflateAnnouncementLister.AnnouncementViewHolder> {

    private Context context;
    private ArrayList<AnnouncementDetails> details;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public InflateAnnouncementLister(Context context,ArrayList<AnnouncementDetails> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_announcements,parent,false);
        return new AnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        AnnouncementDetails announcementDetails = details.get(position);

        String profileUrl = announcementDetails.getProfilePic();
        String posterName = announcementDetails.getName();
        String posterDate = announcementDetails.getDate();
        String announcement = announcementDetails.getMessage();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS", Locale.ENGLISH);
        try {
            Date date = format.parse(posterDate);
            DateFormat dateFormat = new SimpleDateFormat("dd MMM");
            posterDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.get().load(profileUrl).into(holder.profilePicture);
        holder.userName.setText(posterName);
        holder.date.setText(posterDate);
        holder.announcementMessage.setText(announcement);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePicture;
        public TextView announcementMessage;
        public TextView userName;
        public TextView date;

        public AnnouncementViewHolder(View viewItem) {
            super(viewItem);

            profilePicture = viewItem.findViewById(R.id.announcementProfilePic);
            userName = viewItem.findViewById(R.id.userName);
            date = viewItem.findViewById(R.id.dateOfUpload);
            announcementMessage = viewItem.findViewById(R.id.announcementMessage);

            viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
