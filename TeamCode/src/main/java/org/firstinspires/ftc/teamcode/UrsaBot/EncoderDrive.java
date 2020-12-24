package org.firstinspires.ftc.teamcode.UrsaBot;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "EncoderDrive")
public class EncoderDrive extends EasyAuto {

    UrsaPushbot robot = new UrsaPushbot();

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Path", "Starting Pos %7f, %7f, %7f, %7f",
                                                robot.leftFront.getCurrentPosition(),
                                                robot.rightFront.getCurrentPosition(),
                                                robot.leftBack.getCurrentPosition(),
                                                robot.rightBack.getCurrentPosition());
        telemetry.update();

        encoderDrive(driveSpeed, 24, 24, 50);
    }
}
