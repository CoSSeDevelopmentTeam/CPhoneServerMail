package net.comorevi.cpapp.mail.send;

import net.comorevi.cphone.cphone.application.ApplicationManifest;
import net.comorevi.cphone.cphone.model.Bundle;
import net.comorevi.cphone.cphone.model.CustomResponse;
import net.comorevi.cphone.cphone.model.Response;
import net.comorevi.cphone.cphone.widget.activity.ReturnType;
import net.comorevi.cphone.cphone.widget.activity.base.CustomActivity;
import net.comorevi.cphone.cphone.widget.activity.original.MessageActivity;
import net.comorevi.cphone.cphone.widget.element.Element;
import net.comorevi.cphone.cphone.widget.element.Input;
import net.comorevi.cphone.cphone.widget.element.Label;
import net.comorevi.np.sma.util.MailData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SendMailActivity extends CustomActivity {
    private Bundle bundle;
    private MailData mailData;

    public SendMailActivity(ApplicationManifest manifest) {
        super(manifest);
    }

    public SendMailActivity(ApplicationManifest manifest, MailData mailData) {
        super(manifest);
        this.mailData = mailData;
    }

    @Override
    public void onCreate(Bundle bundle) {
        this.bundle = bundle;
        this.setTitle(bundle.getString("send_title"));
        List<Element> elements = new LinkedList<>();
        elements.add(new Label(bundle.getString("send_label1")));
        if (mailData != null) {
            elements.add(new Input(bundle.getString("send_input1_text"), bundle.getString("send_input1_placeholder"), mailData.target));
            elements.add(new Input(bundle.getString("send_input2_text"), bundle.getString("send_input2_placeholder"), mailData.subject));
            elements.add(new Input(bundle.getString("send_input3_text"), bundle.getString("send_input3_placeholder"), mailData.content));
        } else {
            elements.add(new Input(bundle.getString("send_input1_text"), bundle.getString("send_input1_placeholder")));
            elements.add(new Input(bundle.getString("send_input2_text"), bundle.getString("send_input2_placeholder")));
            elements.add(new Input(bundle.getString("send_input3_text"), bundle.getString("send_input3_placeholder")));
        }
        this.addFormElements(elements.toArray(new Element[]{}));
    }

    @Override
    public ReturnType onStop(Response response) {
        CustomResponse customResponse = (CustomResponse) response;
        ArrayList<Object> result = (ArrayList<Object>) customResponse.getResult();
        if (result.get(1).toString().equals("") || result.get(2).toString().equals("") || result.get(3).toString().equals("")) {
            new MessageActivity(getManifest(), bundle.getString("error_title"), bundle.getString("error_few_args"), bundle.getString("error_button3"), bundle.getString("error_button2"), new SendMailActivity(getManifest())).start(bundle);
            return ReturnType.TYPE_CONTINUE;
        } else {
            new SendMailConfirmActivity(getManifest(), new MailData(result.get(2).toString(), result.get(3).toString(), customResponse.getPlayer().getName(), result.get(1).toString())).start(bundle);
            return ReturnType.TYPE_CONTINUE;
        }
    }
}
