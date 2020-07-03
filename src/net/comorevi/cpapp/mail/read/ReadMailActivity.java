package net.comorevi.cpapp.mail.read;

import cn.nukkit.utils.TextFormat;
import net.comorevi.cphone.cphone.application.ApplicationManifest;
import net.comorevi.cphone.cphone.model.Bundle;
import net.comorevi.cphone.cphone.model.ModalResponse;
import net.comorevi.cphone.cphone.model.Response;
import net.comorevi.cphone.cphone.widget.activity.ReturnType;
import net.comorevi.cphone.cphone.widget.activity.base.ModalActivity;
import net.comorevi.np.sma.ServerMailAPI;
import net.comorevi.np.sma.util.MailData;

public class ReadMailActivity extends ModalActivity {
    private Bundle bundle;
    private MailData mailData;

    public ReadMailActivity(ApplicationManifest manifest, MailData mailData) {
        super(manifest);
        this.mailData = mailData;
    }

    @Override
    public void onCreate(Bundle bundle) {
        this.bundle = bundle;
        this.setTitle(mailData.subject);
        this.setContent(
                mailData.content + "\n\n" + TextFormat.GRAY + "ID: " + mailData.databaseId + " FROM: " + mailData.sender
        );
        this.setButton1Text(bundle.getString("read_button_back"));
        this.setButton2Text(bundle.getString("read_button_delete"));

        this.mailData.read = true;
        ServerMailAPI.getInstance().setMailData(mailData.databaseId, mailData);
    }

    @Override
    public ReturnType onStop(Response response) {
        ModalResponse modalResponse = (ModalResponse) response;
        if (modalResponse.isButton1Clicked()) {
            new MailBoxActivity(getManifest()).start(bundle);
            return ReturnType.TYPE_CONTINUE;
        } else {
            ServerMailAPI.getInstance().deleteMail(mailData.databaseId);
            bundle.getCPhone().setHomeMessage(TextFormat.RED + bundle.getString("read_delete_home"));
            return ReturnType.TYPE_END;
        }
    }
}
