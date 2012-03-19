package com.tutorial.domparser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DOMParserActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        final TextView resultView = (TextView)findViewById(R.id.result_textview);
        
        Button requestButton = (Button)findViewById(R.id.start_button);
        requestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                        "<order>" + 
                        "<item Maker=\"Samsung\" Price=\"23000\">Mouse</item>" +
                        "<item Maker=\"LG\" Price=\"12000\">KeyBoard</item>" +
                        "<item Price=\"156000\" Maker=\"Western Digital\">HDD</item>" +
                        "</order>";
                
                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    InputStream istream = new ByteArrayInputStream(xml.getBytes("utf-8"));
                    Document doc = builder.parse(istream);
                    
                    Element order = doc.getDocumentElement();
                    NodeList items = order.getElementsByTagName("item");
                    String result = "";
                    for (int index = 0; index < items.getLength(); index++) {
                        Node item = items.item(index);
                        Node text = item.getFirstChild();
                        String itemName = text.getNodeValue();
                        result += itemName + " : ";
                        
                        NamedNodeMap attrs = item.getAttributes();
                        for (int j = 0; j < attrs.getLength(); j++) {
                            Node attr = attrs.item(j);
                            result += (attr.getNodeName() + " = " + attr.getNodeValue() + " ");
                        }
                        
                        result += "\n";
                    }
                    
                    resultView.setText("주문 목록\n" + result);
                    
                } catch (Exception e) {
                    Toast.makeText(DOMParserActivity.this, "[Exception] " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}