package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Ring_Vision_Test0", group = "Pushbot")

    public class RingVisionTest extends LinearOpMode {
        DrivetrainSubsystem drivetrain;
        VisionSubsystem vision;
        private ElapsedTime runtime = new ElapsedTime();
        int numOfRings = 0;

        @Override
        public void runOpMode(){
            drivetrain = new DrivetrainSubsystem(hardwareMap);
            vision = new VisionSubsystem(hardwareMap, telemetry);

            telemetry.addData("Status", "Ready to run");
            telemetry.update();

            waitForStart();

            numOfRings = vision.getVisionLabel();
            if(numOfRings == 1){
                drivetrain.mecanumDrive_Cartesian(0, 0, .75);
                runtime.reset();
                while(opModeIsActive() && runtime.seconds() < 1){
                    telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                    telemetry.update();
                }
            } else if (numOfRings == 4){
                drivetrain.mecanumDrive_Cartesian(0, -.750, 0);
                runtime.reset();
                while(opModeIsActive() && runtime.seconds() < 2) {
                    telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                    telemetry.update();
                }
            } else {
                drivetrain.mecanumDrive_Cartesian(0, 0, .75);
                runtime.reset();
                while(opModeIsActive()){
                    telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                    telemetry.update();
            }
        }
    }
}
