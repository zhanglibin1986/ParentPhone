package parent.com.parentphone;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author zhanglibin
 * @Project: ParentPhone
 * @Package parent.com.parentphone
 * @date 15/10/19上午10:16
 * @Description
 */
public class ContactViewHolder extends RecyclerView.ViewHolder {
    public ImageView photo;
    public TextView name;

    public ContactViewHolder(View itemView) {
        super(itemView);
        photo = (ImageView) itemView.findViewById(R.id.photo);
        name = (TextView) itemView.findViewById(R.id.text);
    }
}
