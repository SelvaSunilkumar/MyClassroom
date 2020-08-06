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

import edu.education.classroom.Classes.CommentDetails;
import edu.education.classroom.R;

public class InflateCommentLister extends RecyclerView.Adapter<InflateCommentLister.CommentViewHolder> {

    private Context context;
    private ArrayList<CommentDetails> details;

    public InflateCommentLister(Context context,ArrayList<CommentDetails> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_comments,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentDetails commentDetails = details.get(position);

        String userName = commentDetails.getUserName();
        String userProfile = commentDetails.getUserProfile();
        String date = commentDetails.getAnnouncementDate();
        String commentMessage = commentDetails.getCommentMessage();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS", Locale.ENGLISH);
        try {
            Date dater = format.parse(date);
            DateFormat dateFormat = new SimpleDateFormat("dd MMM");
            date = dateFormat.format(dater);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Picasso.get().load(userProfile).into(holder.profilePic);
        holder.commentMessage.setText(commentMessage);
        holder.dateComment.setText(date);
        holder.userName.setText(userName);

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePic;
        public TextView userName;
        public TextView dateComment;
        public TextView commentMessage;

        public CommentViewHolder(View view)
        {
            super(view);
            profilePic = view.findViewById(R.id.profilePic);
            userName = view.findViewById(R.id.name);
            dateComment = view.findViewById(R.id.date);
            commentMessage = view.findViewById(R.id.commentMessage);
        }
    }
}
