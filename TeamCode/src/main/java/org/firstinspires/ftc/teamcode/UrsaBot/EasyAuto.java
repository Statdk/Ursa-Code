package org.firstinspires.ftc.teamcode.UrsaBot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class EasyAuto extends LinearOpMode {

    UrsaHardware robot = new UrsaHardware();

    protected static final double ticksPerInch = 64;

    protected static final double driveSpeed = 0.8;
    protected static  final double turnSpeed = 0.6;

    public ElapsedTime runtime = new ElapsedTime();

    //used to manipulate all motors and is the most flexible out of these functions, but the least optimal.
    public void SetMotorsTime(double lf, double rf, double lb, double rb, long time)
    {
        robot.leftFront.setPower(lf);
        robot.rightFront.setPower(rf);
        robot.leftBack.setPower(lb);
        robot.rightBack.setPower(lb);
        sleep(time);
    }
    //used for manipulating all motors with one line of code in teleop OpModes.
    public void SetMotors(double lf, double rf, double lb, double rb)
    {
        robot.leftFront.setPower(lf);
        robot.rightFront.setPower(rf);
        robot.leftBack.setPower(lb);
        robot.rightBack.setPower(lb);
    }
    //Used to apply one value to all motors opposed to typing out the same numbers 4 times.
    public void SetAllMotorsTime(double Power, long time)
    {
        robot.leftFront.setPower(Power);
        robot.rightFront.setPower(Power);
        robot.leftBack.setPower(Power);
        robot.rightBack.setPower(Power);
        sleep(time);
        //Use this to set all drive motors at once
    }
    //Used to conveniently drive forward.
    public void DriveForward(long time)
    {
        robot.leftFront.setPower(1);
        robot.rightFront.setPower(1);
        robot.leftBack.setPower(1);
        robot.rightBack.setPower(1);
        sleep(time);
    }
    //Used to conveniently drive in reverse.
    public void DriveReverse(long time)
    {
        robot.leftFront.setPower(-1);
        robot.rightFront.setPower(-1);
        robot.leftBack.setPower(-1);
        robot.rightBack.setPower(-1);
        sleep(time);
    }
    //Used to conveniently turn left.
    public void TurnLeft (long time)
    {
        robot.leftFront.setPower(1);
        robot.rightFront.setPower(-1);
        robot.leftBack.setPower(1);
        robot.rightBack.setPower(-1);
        sleep(time);
    }
    //Used to conveniently turn right.
    public void TurnRight (long time)
    {
        robot.leftFront.setPower(-1);
        robot.rightFront.setPower(1);
        robot.leftBack.setPower(-1);
        robot.rightBack.setPower(1);
        sleep(time);
    }
    //Used for precision if your timing motors.
    public void Brake()
    {
        robot.leftFront.setPower(0);
        robot.rightFront.setPower(0);
        robot.leftBack.setPower(0);
        robot.rightBack.setPower(0);
        sleep(1000);
    }
    //Used to conveniently strafe to the left
    public void StrafeLeft(double Power, long time)
    {
        robot.leftFront.setPower(Power);
        robot.rightFront.setPower(-Power);
        robot.leftBack.setPower(-Power);
        robot.rightBack.setPower(Power);
        sleep(time);
    }
    //Used to conveniently strafe to the right.
    public void StrafeRight(double Power, long time)
    {
        robot.leftFront.setPower(-Power);
        robot.rightFront.setPower(Power);
        robot.leftBack.setPower(Power);
        robot.rightBack.setPower(-Power);
        sleep(time);
    }

    public void encoderFoward (double power, double time) {
        encoderDrive(0.6, power, power, power, power, time);
    }

    public void encoderDrive(double speed,
                             double leftFrontInches, double rightFrontInches, double leftBackInches, double rightBackInches,
                             double timeoutS) {
        int newLeftFrontTarget;
        int newRightFrontTarget;
        int newLeftBackTarget;
        int newRightBackTarget;

        if (opModeIsActive()) {

            newLeftFrontTarget = robot.leftFront.getCurrentPosition() + (int)(leftFrontInches * ticksPerInch);
            newRightFrontTarget = robot.rightFront.getCurrentPosition() + (int)(rightFrontInches * ticksPerInch);
            newLeftBackTarget = robot.leftBack.getCurrentPosition() + (int)(leftBackInches * ticksPerInch);
            newRightBackTarget = robot.rightBack.getCurrentPosition() + (int)(rightBackInches * ticksPerInch);
            robot.leftFront.setTargetPosition(newLeftFrontTarget);
            robot.rightFront.setTargetPosition(newRightFrontTarget);
            robot.leftBack.setTargetPosition(newLeftBackTarget);
            robot.rightBack.setTargetPosition(newRightBackTarget);

            robot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            robot.leftFront.setPower(Math.abs(speed));
            robot.rightBack.setPower(Math.abs(speed));
            robot.leftBack.setPower(Math.abs(speed));
            robot.rightBack.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftFront.isBusy() && robot.rightFront.isBusy() &&
                    robot.leftBack.isBusy() && robot.rightBack.isBusy())) {

                //telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                //telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftFront.getCurrentPosition();
                        robot.rightFront.getCurrentPosition();
                        robot.leftBack.getCurrentPosition();
                        robot.rightBack.getCurrentPosition();
                //telemetry.update();
            }

            // Stop all motion
            robot.leftFront.setPower(0);
            robot.rightBack.setPower(0);
            robot.leftFront.setPower(0);
            robot.rightBack.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }
    }

    public void runOpMode() {

        //run nothing here, the only reason this is an opmode is for the sleep method and so it can be extended off of.

    }

}



