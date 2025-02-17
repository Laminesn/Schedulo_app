package edu.fit.schedulo.app.UI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import edu.fit.schedulo.app.R;
import edu.fit.schedulo.app.objs.Category;
import edu.fit.schedulo.app.objs.mood.MoodReport;
import edu.fit.schedulo.app.objs.mood.MoodReports;

import java.time.LocalDate;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoodReportUI#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoodReportUI extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoodReportUI() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoodReport.
     */
    // TODO: Rename and change types and number of parameters
    public static MoodReportUI newInstance(String param1, String param2) {
        MoodReportUI fragment = new MoodReportUI();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mood_report, container, false);

        Spinner categorySpinner = view.findViewById(R.id.SpinnerSource);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.category_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);

        Spinner scaleSpinner = view.findViewById(R.id.feelingScale);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.feeling_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        scaleSpinner.setAdapter(adapter2);



        Button home =  view.findViewById(R.id.home_button);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_moodReport_to_main);
            }
        });

        Button submit =  view.findViewById(R.id.btnSubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve data from the spinners and edittext
                String feelingScale = scaleSpinner.getSelectedItem().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String journalEntry =((EditText) view.findViewById(R.id.usercomment)).getText().toString();

                //create mood object
                short scale = Short.parseShort(feelingScale);
                Category source = Category.valueOf(category);
                MoodReport mood = new MoodReport(scale, source, journalEntry);

                //add mood object to the moodreports
                MoodReports.getInstance().addReport(LocalDate.now(), mood);

                //prints out mood reports (for testing purposes)

                Map<LocalDate, MoodReport> allReports = MoodReports.getInstance().listAllReports();

                for (Map.Entry<LocalDate, MoodReport> entry : allReports.entrySet()) {
                    LocalDate date = entry.getKey();
                    MoodReport report = entry.getValue();

                    System.out.println("Date: " + date);
                    System.out.println("Feeling Scale: " + report.getFeelingScale());
                    System.out.println("Feeling Source: " + report.getFeelingSource());
                    System.out.println("Journal Entry: " + report.getJournalEntry());
                    System.out.println("------------------------");
                }

                Navigation.findNavController(view).navigate(R.id.action_moodReport_to_main);
            }
        });

        return view;
    }
}