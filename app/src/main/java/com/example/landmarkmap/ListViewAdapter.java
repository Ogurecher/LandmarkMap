package com.example.landmarkmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<MarkerOptions> {

    private static final int COMMENT_LENGTH = 46;

    private int resourceLayout;
    private Context context;


    static class ViewHolder {
        TextView textViewName;
        TextView textViewLatLng;
        TextView textViewComment;
        ImageButton buttonLocate;
    }


    public ListViewAdapter(ArrayList<MarkerOptions> markers, int resource, Context context) {
        super(context, resource, markers);
        this.resourceLayout = resource;
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(resourceLayout, null);

            viewHolder = new ViewHolder();

            viewHolder.textViewName = convertView.findViewById(R.id.textViewName);
            viewHolder.textViewLatLng = convertView.findViewById(R.id.textViewLatLng);
            viewHolder.textViewComment = convertView.findViewById(R.id.textViewComment);
            viewHolder.buttonLocate = convertView.findViewById(R.id.buttonLocate);

            viewHolder.buttonLocate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ListView) parent).performItemClick(v, position, 0);
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MarkerOptions marker = getItem(position);

        if (marker != null) {
            if (viewHolder.textViewName != null) {
                viewHolder.textViewName.setText(marker.getTitle());
            }

            if (viewHolder.textViewComment != null) {
                String comment = marker.getSnippet();

                if (comment.length() > COMMENT_LENGTH) { //todo MAGIC NUMBER
                    comment = comment.substring(0, COMMENT_LENGTH) + "..."; //todo MAGIC NUMBER
                }

                viewHolder.textViewComment.setText(comment);
            }

            if (viewHolder.textViewLatLng != null) {
                DecimalFormat decFormat = new DecimalFormat("#.##");
                String coordinates = "(" + decFormat.format(marker.getPosition().latitude) + ", " +
                        decFormat.format(marker.getPosition().longitude) + ")";
                viewHolder.textViewLatLng.setText(coordinates);
            }
        }

        return convertView;
    }
}
