package com.okada.travelogue.Fragment;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.okada.travelogue.Adapters.RecyclerAdapterMessage;
import com.okada.travelogue.HelperClasses.LanguageResourceClass;
import com.okada.travelogue.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

import static android.content.Context.MODE_PRIVATE;


public class ChatPageFragment extends Fragment {

    private TextInputLayout messageLayout;
    private ImageButton sendButton;
    private DatabaseReference reference;
    private SharedPreferences sharedPreferences;
    private String  childNum;
    private ArrayList<String> listKeys,listValues;
    private RecyclerView recyclerView;
    private RecyclerAdapterMessage adapterMessage;
    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_page, container, false);
        initialization(view);
        checkLanguage();
        chatWorks();
        return view;
    }

    private void initialization(View view){
        textView=view.findViewById(R.id.customer_assistant_text);
        messageLayout=view.findViewById(R.id.chat_fragment_message_layout);
        sendButton=view.findViewById(R.id.chat_fragment_send_button);
        recyclerView=view.findViewById(R.id.chat_fragment_recycler_view);
    }

    private void chatWorks() {
        sharedPreferences = getActivity().getSharedPreferences("sharedPreferences", MODE_PRIVATE);
        listKeys=new ArrayList<>();
        listValues=new ArrayList<>();
        childNum=sharedPreferences.getString("childNum",null);
        if (childNum==null){
            sharedPreferences.edit().putString("childNum",System.currentTimeMillis()+"").apply();
            childNum=sharedPreferences.getString("childNum",System.currentTimeMillis()+"");
        }
        adapterMessage=new RecyclerAdapterMessage(listKeys,listValues,getActivity());
        LinearLayoutManager manager=new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterMessage);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked();
            }
        });
        reference= FirebaseDatabase.getInstance().getReference();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listKeys.clear();
                listValues.clear();
                HashMap<String,String > map=new HashMap<>();
                for(DataSnapshot ds : snapshot.child("messages").child("message"+childNum).getChildren()) {
                    map.put(ds.getKey(),(String) ds.getValue());
                }
                TreeMap<String,String> treeMap=new TreeMap<>(map);
                ArrayList<String> arrKeys=new ArrayList<>(treeMap.keySet());
                ArrayList<String> arrValues=new ArrayList<>(treeMap.values());

                for (int i = arrKeys.size() - 1; i >= 0; i--) {
                    listKeys.add(arrKeys.get(i));
                    listValues.add(arrValues.get(i));
                }
                recyclerView.smoothScrollToPosition(0);
                adapterMessage.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkLanguage() {
        Resources resources= LanguageResourceClass.getResource(getActivity());
        textView.setText(resources.getString(R.string.customer_assistant));
        messageLayout.setHint(resources.getString(R.string.enter_message));
    }

    public void clicked(){
        String message=messageLayout.getEditText().getText().toString().trim();
        messageLayout.getEditText().setText("");
        if (!message.equals("")){
            reference.child("messages").child("message"+childNum).child(System.currentTimeMillis()+"").setValue(message);
            recyclerView.smoothScrollToPosition(0);
            adapterMessage.notifyDataSetChanged();
        }
    }

}