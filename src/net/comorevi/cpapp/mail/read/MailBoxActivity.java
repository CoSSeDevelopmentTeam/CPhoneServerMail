package net.comorevi.cpapp.mail.read;

import net.comorevi.cpapp.mail.MainActivity;
import net.comorevi.cphone.cphone.application.ApplicationManifest;
import net.comorevi.cphone.cphone.model.Bundle;
import net.comorevi.cphone.cphone.model.ListResponse;
import net.comorevi.cphone.cphone.model.Response;
import net.comorevi.cphone.cphone.widget.activity.ReturnType;
import net.comorevi.cphone.cphone.widget.activity.base.ListActivity;
import net.comorevi.cphone.cphone.widget.element.Button;
import net.comorevi.np.sma.ServerMailAPI;
import net.comorevi.np.sma.util.MailData;

import java.util.LinkedList;
import java.util.List;

public class MailBoxActivity extends ListActivity {
    private Bundle bundle;
    private List<MailData> mailDataList = new LinkedList<>();

    public MailBoxActivity(ApplicationManifest manifest) {
        super(manifest);
    }

    @Override
    public void onCreate(Bundle bundle) {
        this.bundle = bundle;
        this.setTitle(bundle.getString("mailbox_title"));
        this.setContent(bundle.getString("mailbox_content"));
        mailDataList.addAll(ServerMailAPI.getInstance().getMailBox(bundle.getCPhone().getPlayer().getName()));
        mailDataList.forEach(
                mailData -> {
                    this.addButton(new Button((mailData.read ? "§r" : "§6") + mailData.subject));
                }
        );
    }

    @Override
    public ReturnType onStop(Response response) {
        ListResponse listResponse = (ListResponse) response;
        if (listResponse.getButtonIndex() == -1) {
            new MainActivity(getManifest()).start(bundle);
            return ReturnType.TYPE_CONTINUE;
        } else {
            new ReadMailActivity(getManifest(), mailDataList.get(listResponse.getButtonIndex())).start(bundle);
            return ReturnType.TYPE_CONTINUE;
        }
    }
}
