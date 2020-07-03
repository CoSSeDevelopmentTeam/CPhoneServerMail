package net.comorevi.cpapp.mail.send;

import cn.nukkit.utils.TextFormat;
import net.comorevi.cphone.cphone.application.ApplicationManifest;
import net.comorevi.cphone.cphone.model.Bundle;
import net.comorevi.cphone.cphone.model.ModalResponse;
import net.comorevi.cphone.cphone.model.Response;
import net.comorevi.cphone.cphone.widget.activity.ReturnType;
import net.comorevi.cphone.cphone.widget.activity.base.ModalActivity;
import net.comorevi.np.sma.ServerMailAPI;
import net.comorevi.np.sma.util.MailData;

public class SendMailConfirmActivity extends ModalActivity {
    private Bundle bundle;
    private MailData mailData;

    public SendMailConfirmActivity(ApplicationManifest manifest, MailData mailData) {
        super(manifest);
        this.mailData = mailData;
    }

    @Override
    public void onCreate(Bundle bundle) {
        this.bundle = bundle;
        this.setTitle(bundle.getString("send_confirm_title"));
        this.setContent(
                "宛先/To : " + mailData.target + "\n件名/Subject : " + mailData.subject + "\n内容/Content : " + mailData.content
        );
        this.setButton1Text(bundle.getString("send_confirm_button1"));
        this.setButton2Text(bundle.getString("send_confirm_button2"));
    }

    @Override
    public ReturnType onStop(Response response) {
        ModalResponse modalResponse = (ModalResponse) response;
        if (modalResponse.isButton1Clicked()) {
            ServerMailAPI.getInstance().sendMail(mailData);
            bundle.getCPhone().setHomeMessage(TextFormat.AQUA + bundle.getString("send_confirm_home"));
            return ReturnType.TYPE_END;
        } else {
            new SendMailActivity(getManifest(), mailData).start(bundle);
            return ReturnType.TYPE_CONTINUE;
        }
    }
}
