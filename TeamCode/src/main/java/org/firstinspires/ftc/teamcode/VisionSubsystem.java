package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;


public class VisionSubsystem  {
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY = "AYcChBn/////AAABmY183vEhzE" +
            "9BlrJknwN+gRtU/NR46gxnIKh/7HuMYk1TP834tuS7U1xHtrKdVMakKHeXN+vi3M" +
            "raHTIZENUZld6kqPa+Bf6rQWp2ZeWe0Trh/7oBf9VWcfl05lfsSCRQLg6Cfg0fuc" +
            "Yoc0tT7tcAI42kzReGF3u7pEH+QmeVR0vTd9d5yAcEMqMAlfO3EOZA3hhSIcZAnfA" +
            "p1QvstfgXpn/yBVOWqjuNYPtdfPP73Z7RH1vqq25o0HTDrATXTy2C7HtFCPD91ha+Y" +
            "gB5XpZTPxOJPVnhnVfhinn8vxeRzgkBsN1fntRWH2e8/Gp3jqZrH2sbVLnhwVPAXgV+t" +
            "IeIWkX/c2ZHtpyMj/99rWfENs7g";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    public String visionLabel;


    public VisionSubsystem(HardwareMap hardwareMap, Telemetry telemetry) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "cam0");;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        visionLabel = "default";

        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
    }

    public void startupVision() {
        initTfod();
        activateTfod();
    }

    private void activateTfod() {
        if (tfod != null) {
            tfod.activate();
        }
    }

    public void deactivateTfod() {
        if (tfod != null) {
            tfod.shutdown();
        }
    }

    public void runVisionSystem() {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 0) {
                    // empty list.  no objects recognized.
                    telemetry.addData("TFOD", "No items detected.");
                    telemetry.addData("Target Zone", "A");
                } else {
                    // list is not empty.
                    // step through the list of recognitions and display boundary info.
                    int i = 0;
                    for (Recognition recognition : updatedRecognitions) {
                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());

                        String rings;
                        // check label to see which target zone to go after.
                        if (recognition.getLabel().equals("Single")) {
                            telemetry.addData("Target Zone", "B");
                            visionLabel = "Single";
                        } else if (recognition.getLabel().equals("Quad")) {
                            telemetry.addData("Target Zone", "C");
                            visionLabel = "Quad";
                        } else {
                            telemetry.addData("Target Zone", "UNKNOWN");
                            visionLabel = "NO";

                        }
                    }
                }
                telemetry.update();
            }
        }
    }

    public int getVisionLabel() {
        int x;
        if (visionLabel.equals("Single")) {
            x = 1;
        } else if (visionLabel.equals("Quad")) {
            x = 4;
        } else {
            x = 0;
        }
        return x;
    }

    /* Initialize the TensorFlow Object Detection engine. */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}