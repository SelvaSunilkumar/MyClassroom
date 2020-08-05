package edu.education.classroom.Classes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import edu.education.classroom.R;

public class InflateClassLister extends RecyclerView.Adapter<InflateClassLister.ClassViewHolder> {

    private Context context;
    private ArrayList<ClassDetails> details;
    private OnItemClickListener listener;
    private int backgroundColours[] = {R.color.color1,R.color.color2,R.color.color3,R.color.color4};
    int index = 0;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public InflateClassLister(Context context,ArrayList<ClassDetails> details)
    {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_classes,parent,false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassDetails classDetails = details.get(position);

        String className = classDetails.getClassName();
        String classSection = classDetails.getClassSection();
        String classDescription = classDetails.getClassDesription();
        int classBackground = classDetails.getBackgroundNumber();

        holder.ClassName.setText(className);
        holder.ClassSection.setText(classSection);
        holder.ClassDescription.setText(classDescription);
        int backColor[] = {R.drawable.classbackground1,R.drawable.classbackground2,R.drawable.classbackground3,R.drawable.classbackground4,R.drawable.classbackground5};
        holder.linearLayout.setBackground(context.getDrawable(backColor[classBackground]));
    }


    @Override
    public int getItemCount() {
        return details.size();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder
    {
        public TextView ClassName;
        public TextView ClassSection;
        public TextView ClassDescription;
        public LinearLayout linearLayout;

        public ClassViewHolder(View viewItem)
        {
            super(viewItem);

            ClassName = viewItem.findViewById(R.id.className);
            ClassSection = viewItem.findViewById(R.id.classSection);
            ClassDescription = viewItem.findViewById(R.id.classDescription);
            linearLayout = viewItem.findViewById(R.id.linearLayout);

            viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
