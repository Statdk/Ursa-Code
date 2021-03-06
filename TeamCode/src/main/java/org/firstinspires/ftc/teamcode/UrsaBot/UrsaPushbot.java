package org.firstinspires.ftc.teamcode.UrsaBot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

//hardware mapping for the robot, please contribute when necessary.
public class UrsaPushbot
{

    public DcMotor leftFront    = null;
    public DcMotor rightFront   = null;
    public DcMotor leftBack     = null;
    public DcMotor rightBack    = null;
    public DcMotor wobbleGoalRot = null;
    public DcMotor intakeMotor  = null;
    public DcMotor launcherMotor = null;
    public Servo wobbleGoalLifter = null;
    public Servo wobbleGoalPusher = null;
    public Servo launcherFire = null;
    boolean intakeToggle = true;
    boolean intakeUpToggle = true;
    double DRIVE_SPEED = 0.6;
    double TURN_SPEED = 0.35;
    double launcherSpeed = 1;

    public static final double BRAKE = 0;

    HardwareMap UrsaMap           = null;

    public UrsaPushbot(){

    }

    public void init(HardwareMap ahwMap) {

        UrsaMap = ahwMap;

        leftFront        = UrsaMap.get(DcMotor.class, "leftFront");
        rightFront       = UrsaMap.get(DcMotor.class, "rightFront");
        leftBack         = UrsaMap.get(DcMotor.class, "LeftBack");
        rightBack        = UrsaMap.get(DcMotor.class, "rightBack");
        launcherMotor    = UrsaMap.get(DcMotor.class, "launcherMotor");
        wobbleGoalLifter = UrsaMap.get(Servo.class, "wobbleGoalLifter");
        wobbleGoalPusher = UrsaMap.get(Servo.class, "wobbleGoalPusher");

        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        launcherMotor.setDirection(DcMotor.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

        wobbleGoalPusher.setPosition(0);
        wobbleGoalLifter.setPosition(0);
    }
}