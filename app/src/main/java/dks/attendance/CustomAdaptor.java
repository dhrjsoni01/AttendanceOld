package dks.attendance;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import java.util.ArrayList;

/**
 * Created by DKS on 2/24/2017.
 */

public class CustomAdaptor extends ArrayAdapter<model> {

    ArrayList<model> data = new ArrayList<>();

    public CustomAdaptor(Context context, int resource, ArrayList<model> objects) {
        super(context, resource, objects);
        data = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.row2, null);
        TextView firstline= (TextView) v.findViewById(R.id.sub_name);
        TextView  secondline= (TextView) v.findViewById(R.id.present);
        TextView  thirdline= (TextView) v.findViewById(R.id.facuty_name);
        firstline.setText(data.get(position).getSubject_name());
        secondline.setText(data.get(position).getStudent_data());
        thirdline.setText(data.get(position).getFaculty_name());
        return v;

    }
}
