package org.firstinspires.ftc.teamcode.UrsaBot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

//hardware mapping for the robot, please contribute when necessary.
public class UrsaHardware
{

    public DcMotor leftFront    = null;
    public DcMotor rightFront   = null;
    public DcMotor leftBack     = null;
    public DcMotor rightBack    = null;
    public DcMotor wobbleGoalRot = null;
    public DcMotor intakeMotor  = null;
    public DcMotor intakeUp     = null;
    public Servo wobbleGoalLifter = null;
    public Servo wobbleGoalPusher = null;
    boolean intakeToggle = true;
    boolean intakeUpToggle = true;

    // Constants
    public final double     COUNTS_PER_INCH         = 62;
    public final double     DRIVE_SPEED             = 0.6;
    public final double     TURN_SPEED              = 0.35;
    public final double     TILE_SIZE               = 24;

    public static final double BRAKE = 0;

    HardwareMap UrsaMap           = null;

    public UrsaHardware(){

    }

    public void init(HardwareMap ahwMap) {

        UrsaMap = ahwMap;

        leftFront        = UrsaMap.get(DcMotor.class, "leftFront");
        rightFront       = UrsaMap.get(DcMotor.class, "rightFront");
        leftBack         = UrsaMap.get(DcMotor.class, "LeftBack");
        rightBack        = UrsaMap.get(DcMotor.class, "rightBack");
        wobbleGoalLifter = UrsaMap.get(Servo.class, "wobbleGoalLifter");
        wobbleGoalPusher = UrsaMap.get(Servo.class, "wobbleGoalPusher");

        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFront.setPower(0);
        rightFront.setPower(0);
        leftBack.setPower(0);
        rightBack.setPower(0);

        wobbleGoalPusher.setPosition(0);
        wobbleGoalLifter.setPosition(0);
    }
}