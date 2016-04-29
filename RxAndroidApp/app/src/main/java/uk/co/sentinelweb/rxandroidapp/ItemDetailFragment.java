package uk.co.sentinelweb.rxandroidapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.sentinelweb.rxandroidapp.dummy.DummyContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    @Bind(R.id.item_detail)
    TextView itemDetail;
    @Bind(R.id.item_detail_edit)
    EditText itemDetailEdit;
    @Bind(R.id.item_content_edit)
    EditText itemContentEdit;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            DummyContent.getItems()
                    .filter(item -> item.id.equals(getArguments().getString(ARG_ITEM_ID)))
                    .subscribe(item -> itemLoaded(item));// loads after from io thread
        }
    }

    private void itemLoaded(final DummyContent.DummyItem item) {
        mItem = item;
        updateAppBar();
        updateItemView();
    }

    private void updateAppBar() {
        final Activity activity = this.getActivity();
        final CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.getContent());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        ButterKnife.bind(this, rootView);
        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            updateItemView();
        }

        return rootView;
    }

    private void updateItemView() {
        itemDetail.setText(mItem.getDetails());
        itemDetailEdit.setText(mItem.getDetails());
        itemContentEdit.setText(mItem.getContent());
        // if you want multiple observers do this.
//            final ConnectableObservable<CharSequence> publish = RxTextView.textChanges(edit).publish();
//            publish.subscribe(cs -> text.setText(cs));
//            publish.connect();
        RxTextView.textChanges(itemDetailEdit).subscribe(cs -> {
            itemDetail.setText(cs);
            mItem.setDetails(cs.toString());
        });
        RxTextView.textChanges(itemContentEdit).subscribe(cs -> {
            mItem.setContent(cs.toString());
            updateAppBar();
        });

    }
}
