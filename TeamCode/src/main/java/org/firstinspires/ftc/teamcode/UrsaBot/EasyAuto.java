package org.firstinspires.ftc.teamcode.UrsaBot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class EasyAuto extends LinearOpMode {

    UrsaPushbot robot = new UrsaPushbot();

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

    public void runOpMode() {

        //run nothing here, the only reason this is an opmode is for the sleep function and so it can be extended off of.

    }

}



