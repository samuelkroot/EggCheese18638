/*
17012
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

//////////////////////////////////////////////////////////////////////////////////////////
@TeleOp(name="Test Mode with Vision", group="Iterative Opmode")
// @Disabled        // Comment/Uncomment this line as needed to show/hide this opmode
//////////////////////////////////////////////////////////////////////////////////////////

public class TestModeBetaWithVision extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();
    Servo testServo;
    //ShooterSubsystem shooter;
    //Servo revServo;
    VisionSubsystem vision;
    WebcamName theWebcam;
//////////////////////////////////////////////////////////////////////////////////////////

    /* Code to run ONCE when the driver hits INIT */
    @Override
    public void init() {
        //shooter = new ShooterSubsystem(this.hardwareMap);
        vision = new VisionSubsystem(this.hardwareMap, this.telemetry);

        // temporary adds for testing purposes
        testServo = hardwareMap.get(Servo.class, "testservo");
        //revServo = hardwareMap.get(Servo.class, "revservo");
        //myServo.setDirection(REVERSE);

        // Set up our telemetry dashboard
        getTelemetry();

        // Vision Setup
        vision.startupVision();

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized    :)");
    }

//////////////////////////////////////////////////////////////////////////////////////////

    /* Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY */
    @Override
    public void init_loop() {
    }

//////////////////////////////////////////////////////////////////////////////////////////

    /* Code to run ONCE when the driver hits PLAY */
    @Override
    public void start() {
        runtime.reset();
        getTelemetry();
    }

//////////////////////////////////////////////////////////////////////////////////////////

    /* Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP */
    @Override
    public void loop() {

        double flywheelValue = 0.5;
        double acceleratorValue = 0.8;

        /*if (gamepad1.left_trigger == 1) {
            myServo.setPosition(0.6);
            revServo.setPosition(0.6);
        } else {
            myServo.setPosition(0);
            revServo.setPosition(0);
        }


        if (gamepad1.cross) {
            shooter.setShooter(flywheelValue, acceleratorValue);
            //shooter.setFlywheel(0.5);
            telemetry.addLine("Shoot the pew pew. \nBoth motors should be spinning.");
        } else if (gamepad1.square) {
            shooter.setFlywheel(-0.5);
            telemetry.addData("Flying wheel spinning with value of ", flywheelValue);
        } else if (gamepad1.circle) {
            shooter.setAccelerator(acceleratorValue);
            telemetry.addData("Accelerator wheel spinning with value of ", acceleratorValue);
        } else if (gamepad1.triangle) {
            telemetry.addLine("TRIANGLE PUSHEDy");
            //myServo.setPower(.8);
            revServo.setPosition(.8);
        } else {                        // stop all
            shooter.stopAll();
            //myServo.setPower(0);
            //revServo.setPosition(0);
        }*/

        vision.runVisionSystem();

        if (vision.getVisionLabel()==1) {
            //vroom vroom
            testServo.setPosition(0.8);
        } else if (vision.getVisionLabel()==4) {
            //different vroom vroom
            testServo.setPosition(0.2);
        } else {
            //do nothing :P
        }

        // Call Telemetry
        getTelemetry();

    }

//////////////////////////////////////////////////////////////////////////////////////////

    /* Code to run ONCE after the driver hits STOP */
    @Override
    public void stop() {
        vision.deactivateTfod();
        telemetry.addData("Robot Stopped. ", "Have a nice day.");
        telemetry.addData("Final runtime: ", runtime.toString());
        telemetry.update();
    }

//////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////
    /*                              TELEOP-SPECIFIC METHODS                                 */
//////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////

    public void getTelemetry() {
        // Show the elapsed game time
        telemetry.addData("Run Time: ", runtime.toString());

        // Telemetry about motion
        //telemetry.addData("Motors", "leftFront (%.2f), rightFront (%.2f), rightRear (%.2f), leftRear (%.2f)", telemValues[0], telemValues[1], telemValues[2], telemValues[3]);
        telemetry.update();
    }  // getTelemetry

}    // The Almighty Curly Brace For Everything