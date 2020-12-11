package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class DrivetrainSubsystem {
    DcMotor frontLeft, frontRight, backLeft, backRight;

    public DrivetrainSubsystem(HardwareMap hardwareMap) {
        frontRight = hardwareMap.get(DcMotor.class, "FR");
        frontLeft = hardwareMap.get(DcMotor.class, "FL");
        backLeft = hardwareMap.get(DcMotor.class, "BL");
        backRight = hardwareMap.get(DcMotor.class, "BR");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
    }

    public void stopDriving() {
        setMotors(0, 0, 0, 0);
    }

    public void setMotors(double FL, double BL, double FR, double BR) {
        frontLeft.setPower(FL);
        frontRight.setPower(FR);
        backLeft.setPower(BL);
        backRight.setPower(BR);
    }

    public void mecanumDrive_Cartesian(double x, double y, double rotation) {
        double wheelSpeeds[] = new double[4];

        x = deadband(x);
        y = deadband(y);
        rotation = deadband(rotation);

        x = x * x * x;
        y = y * y * y;
        rotation = rotation * rotation * rotation;

        wheelSpeeds[0] = -x + y + rotation;
        wheelSpeeds[1] = x + y + rotation;
        wheelSpeeds[2] = x + y - rotation;
        wheelSpeeds[3] = -x + y - rotation;

        wheelSpeeds = normalize(wheelSpeeds);

        setMotors(wheelSpeeds[0], wheelSpeeds[1], wheelSpeeds[2], wheelSpeeds[3]);

    }

    public void turnToAngle(){}
        Orientation angles;
        double target = 90;
        double tolerance = 5;
        double speed = .5;
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        if (Math.abs(target - angles.firstAngle) > tolerance) {
            drivetrain.setMotors(-speed, -speed, speed, speed);
        } else {
            drivetrain.setMotors(0, 0, 0, 0);
        }

    private double[] normalize(double[] wheelSpeeds) {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);

        for (int i = 1; i < wheelSpeeds.length; i++) {
            double magnitude = Math.abs(wheelSpeeds[i]);
            if (magnitude > maxMagnitude) {
                maxMagnitude = magnitude;
            }
        }

        if (maxMagnitude > 1.0) {
            for (int i = 0; i < wheelSpeeds.length; i++) {
                wheelSpeeds [i] /= maxMagnitude;
            }
        }
        return wheelSpeeds;
    }

    private double deadband(double x) {
        if (Math.abs(x) < .1) {
            x = 0;
        }
        return x;
    }

}
