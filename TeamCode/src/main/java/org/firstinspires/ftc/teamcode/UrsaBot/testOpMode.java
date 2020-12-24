package org.firstinspires.ftc.teamcode.UrsaBot;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TestOpMode")
public class testOpMode extends LinearOpMode {

    UrsaPushbot robot = new UrsaPushbot();

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Set up");
        telemetry.update();

        robot.init(hardwareMap);

        waitForStart();
        //runtime.reset();

        while (opModeIsActive()) {

            //Mecanum drive code, same used in onBot
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;


            robot.leftFront.setPower(y + x + rx);
            robot.rightFront.setPower(y - x - rx);
            robot.leftBack.setPower(y - x + rx);
            robot.rightBack.setPower(y + x - rx);

            //unable to figure out how to show motor powers, if you know how, replace this lin with the code.
            telemetry.addData("Run Time", runtime.toString());
            telemetry.update();
        }
    }
}

