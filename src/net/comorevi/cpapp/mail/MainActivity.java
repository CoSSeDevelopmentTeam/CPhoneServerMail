package net.comorevi.cpapp.mail;

import net.comorevi.cpapp.mail.read.MailBoxActivity;
import net.comorevi.cpapp.mail.send.SendMailActivity;
import net.comorevi.cphone.cphone.application.ApplicationManifest;
import net.comorevi.cphone.cphone.model.Bundle;
import net.comorevi.cphone.cphone.model.ListResponse;
import net.comorevi.cphone.cphone.model.Response;
import net.comorevi.cphone.cphone.widget.activity.ReturnType;
import net.comorevi.cphone.cphone.widget.activity.base.ListActivity;
import net.comorevi.cphone.cphone.widget.activity.original.MessageActivity;
import net.comorevi.cphone.cphone.widget.element.Button;
import net.comorevi.np.sma.ServerMailAPI;

import java.util.Objects;

public class MainActivity extends ListActivity {
    private Bundle bundle;

    public MainActivity(ApplicationManifest manifest) {
        super(manifest);
    }

    @Override
    public void onCreate(Bundle bundle) {
        this.bundle = bundle;
        this.setTitle(bundle.getString("main_title"));
        int unread = ServerMailAPI.getInstance().existsMailData(bundle.getCPhone().getPlayer().getName()) ? (int) ServerMailAPI.getInstance().getMailBox(bundle.getCPhone().getPlayer().getName()).stream().filter(mailData -> !mailData.read).count() : 0 ;
        this.setContent(bundle.getString("main_content").replace("{unread}", String.valueOf(unread)));
        Button[] buttons = {
                new Button(bundle.getString("main_button1")),
                new Button(bundle.getString("main_button2"))
        };
        this.addButtons(buttons);
    }

    @Override
    public ReturnType onStop(Response response) {
        ListResponse listResponse = (ListResponse) response;
        switch (listResponse.getButtonIndex()) {
            case 0:
                if (ServerMailAPI.getInstance().existsMailData(listResponse.getPlayer().getName())) {
                    new MailBoxActivity(getManifest()).start(bundle);
                } else {
                    new MessageActivity(getManifest(), bundle.getString("error_title"), bundle.getString("error_no_received_mail"), bundle.getString("error_button1"), bundle.getString("error_button2"), new MainActivity(getManifest())).start(bundle);
                }
                return ReturnType.TYPE_CONTINUE;
            case 1:
                new SendMailActivity(getManifest()).start(bundle);
                return ReturnType.TYPE_CONTINUE;
        }
        return ReturnType.TYPE_END;
    }
}
