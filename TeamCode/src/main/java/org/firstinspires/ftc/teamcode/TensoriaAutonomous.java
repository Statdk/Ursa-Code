/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.UrsaBot.UrsaHardware;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Tensoria Drive", group = "Tensoria")
public class TensoriaAutonomous extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY =
            "ASQvetT/////AAABmWgxsfDXYU6IkQfbRy6xzm449PyiWSSWyoYGxxxU1QQMvUdOnEyUJc2vEm2wdOUV0xBsN6N2wGY6O4RoMxhMW/PiuQ6w3bXBGhy9O7v86Org3IfjnP8EIJZNuKqls2trtBJQVQA5BGk5/5LUuJ7BpHFCaSLlRXpw7Fjr32ot6k12o9/5R1Cl1G744MIixVPxj7RJPWRGncoecju71NRbupFEGfxhBO3Bk49M3vtYEWkyUXjEbCPoCeYJ4+/BQlpMVNWBXT7ampzUnClI86RG1/8athMdklv0XN+iETWZMLrU/TuFvWCn+gk4F1amGjttNUNPQiEIjQY7geUXRX23Jc7Fsn/rEKbEVYw+mynLoaMn";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;


    private static final CameraDirection CAMERA_CHOICE = BACK;
    private static final boolean PHONE_IS_PORTRAIT = false;

    // Since ImageTarget trackables use mm to specifiy their dimensions, we must use mm for all the physical dimension.
    // We will define some constants and conversions here
    private static final float mmPerInch = 25.4f;
    private static final float mmTargetHeight = (6) * mmPerInch;          // the height of the center of the target image above the floor

    // Constants for perimeter targets
    private static final float halfField = 72 * mmPerInch;
    private static final float quadField = 36 * mmPerInch;

    // Class Members
    private OpenGLMatrix lastLocation = null;
    private boolean targetVisible = false;
    private float phoneXRotate = 0;
    private float phoneYRotate = 0;
    private float phoneZRotate = 0;

    private boolean isDetecting = true;

    UrsaHardware robot = new UrsaHardware();   // Hardware class

    private static final boolean ursaBot = false;
    private static final boolean UseWebcam = true;
    WebcamName webcamName;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        //region Tensoria Setup
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.

        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */

        if (UseWebcam) webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        if (UseWebcam) parameters.cameraName = webcamName;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.

        // Load the data sets for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        VuforiaTrackables targetsUltimateGoal = this.vuforia.loadTrackablesFromAsset("UltimateGoal");
        VuforiaTrackable blueTowerGoalTarget = targetsUltimateGoal.get(0);
        blueTowerGoalTarget.setName("Blue Tower Goal Target");
        VuforiaTrackable redTowerGoalTarget = targetsUltimateGoal.get(1);
        redTowerGoalTarget.setName("Red Tower Goal Target");
        VuforiaTrackable redAllianceTarget = targetsUltimateGoal.get(2);
        redAllianceTarget.setName("Red Alliance Target");
        VuforiaTrackable blueAllianceTarget = targetsUltimateGoal.get(3);
        blueAllianceTarget.setName("Blue Alliance Target");
        VuforiaTrackable frontWallTarget = targetsUltimateGoal.get(4);
        frontWallTarget.setName("Front Wall Target");

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsUltimateGoal);

        /**
         * In order for localization to work, we need to tell the system where each target is on the field, and
         * where the phone resides on the robot.  These specifications are in the form of <em>transformation matrices.</em>
         * Transformation matrices are a central, important concept in the math here involved in localization.
         * See <a href="https://en.wikipedia.org/wiki/Transformation_matrix">Transformation Matrix</a>
         * for detailed information. Commonly, you'll encounter transformation matrices as instances
         * of the {@link OpenGLMatrix} class.
         *
         * If you are standing in the Red Alliance Station looking towards the center of the field,
         *     - The X axis runs from your left to the right. (positive from the center to the right)
         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
         *
         * Before being transformed, each target image is conceptually located at the origin of the field's
         *  coordinate system (the center of the field), facing up.
         */

        //Set the position of the perimeter targets with relation to origin (center of field)
        redAllianceTarget.setLocation(OpenGLMatrix
                .translation(0, -halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        blueAllianceTarget.setLocation(OpenGLMatrix
                .translation(0, halfField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));
        frontWallTarget.setLocation(OpenGLMatrix
                .translation(-halfField, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

        // The tower goal targets are located a quarter field length from the ends of the back perimeter wall.
        blueTowerGoalTarget.setLocation(OpenGLMatrix
                .translation(halfField, quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));
        redTowerGoalTarget.setLocation(OpenGLMatrix
                .translation(halfField, -quadField, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        //
        // Create a transformation matrix describing where the phone is on the robot.
        //
        // NOTE !!!!  It's very important that you turn OFF your phone's Auto-Screen-Rotation option.
        // Lock it into Portrait for these numbers to work.
        //
        // Info:  The coordinate frame for the robot looks the same as the field.
        // The robot's "forward" direction is facing out along X axis, with the LEFT side facing out along the Y axis.
        // Z is UP on the robot.  This equates to a bearing angle of Zero degrees.
        //
        // The phone starts out lying flat, with the screen facing Up and with the physical top of the phone
        // pointing to the LEFT side of the Robot.
        // The two examples below assume that the camera is facing forward out the front of the robot.

        // We need to rotate the camera around it's long axis to bring the correct camera forward.
        if (CAMERA_CHOICE == BACK) {
            phoneYRotate = -90;
        } else {
            phoneYRotate = 90;
        }

        // Rotate the phone vertical about the X axis if it's in portrait mode
        if (PHONE_IS_PORTRAIT) {
            phoneXRotate = 90;
        }

        // Next, translate the camera lens to where it is on the robot.
        // In this example, it is centered (left to right), but forward of the middle of the robot, and above ground level.
        final float CAMERA_FORWARD_DISPLACEMENT = 4.0f * mmPerInch;   // eg: Camera is 4 Inches in front of robot center
        final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;   // eg: Camera is 8 Inches above ground
        final float CAMERA_LEFT_DISPLACEMENT = 0;     // eg: Camera is ON the robot's center line

        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

        /*  Let all the trackable listeners know where the phone is.  */
        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }

        // WARNING:
        // In this sample, we do not wait for PLAY to be pressed.  Target Tracking is started immediately when INIT is pressed.
        // This sequence is used to enable the new remote DS Camera Preview feature to be used with this sample.
        // CONSEQUENTLY do not put any driving commands in this loop.
        // To restore the normal opmode structure, just un-comment the following line:

        // waitForStart();

        // Note: To use the remote camera preview:
        // AFTER you hit Init on the Driver Station, use the "options menu" to select "Camera Stream"
        // Tap the preview window to receive a fresh image.

        targetsUltimateGoal.activate();


//        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 1.78 or 16/9).

            // Uncomment the following line if you want to adjust the magnification and/or the aspect ratio of the input images.
            //tfod.setZoom(2.5, 1.78);
        }

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //endregion

        //region Encoder Setup
        robot.leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if (!ursaBot) {
            robot.leftFront.setDirection(DcMotor.Direction.FORWARD);
            robot.rightFront.setDirection(DcMotor.Direction.FORWARD);
            robot.leftBack.setDirection(DcMotor.Direction.REVERSE);
            robot.rightBack.setDirection(DcMotor.Direction.REVERSE);
            robot.TILE_SIZE = (double) robot.TILE_SIZE * 0.8 /*20/25 teeth*/;
            //Change encoder ticks per inch next...
        }

        //endregion

        waitForStart();
        runtime.reset();
        if (opModeIsActive()) {

            List<Recognition> tfodRecogs = getTfodRecognitions();

            getVuforia(allTrackables);

            sleep(1000);

            // Filter the recognitions
            Recognition largestRecog = null;
            for (int i = 0; i < tfodRecogs.size(); i++) {
                if (largestRecog == null) largestRecog = tfodRecogs.get(i);

                if (tfodRecogs.get(i).getHeight() > largestRecog.getHeight())
                    largestRecog = tfodRecogs.get(i);
            }

            if (largestRecog != null) {
                if (largestRecog.getHeight() < 70)
                    largestRecog = null;
            }

            int TargetZone;
            if (largestRecog == null) { // No rings found, go to tile A
                telemetry.addData("Tile", "A");
                encoderDrive(robot.DRIVE_SPEED, 72, 72, 72, 72, 5.0); // forward 3 tiles
                encoderDrive(robot.TURN_SPEED, 24, -24, 24, -24, 5.0); // 90° left turn                encoderDrive(robot.DRIVE_SPEED, 72, 72, 72, 72, 5.0); // forward 3 tiles
                encoderDrive(robot.DRIVE_SPEED, 24, 24, 24, 24, 5.0); // forward 1 tile
                TargetZone = 1; //Keep track of Target Zone
            } else if (largestRecog.getHeight() > /* SOME CONSTANT */ 150) { // Four ring found, go to tile C
                telemetry.addData("Tile", "C");
                encoderDrive(robot.DRIVE_SPEED, 120, 120, 120, 120, 10.0); // forward 5 tiles
                encoderDrive(robot.TURN_SPEED, 24, -24, 24, -24, 5.0); // 90° left turn                encoderDrive(robot.DRIVE_SPEED, 72, 72, 72, 72, 5.0); // forward 3 tiles
                encoderDrive(robot.DRIVE_SPEED, 24, 24, 24, 24, 5.0); // forward 1 tile
                TargetZone = 3; //Keep track of Target Zone
            } else { // Middle ring height, go to tile B
                telemetry.addData("Tile", "B");
                encoderDrive(robot.DRIVE_SPEED, 96, 96, 96, 96, 5.0); // forward 4 tiles
                TargetZone = 2; //Keep track of Target Zone
            }

            if (largestRecog != null) {
                telemetry.addData("Recognition Height", largestRecog.getHeight());
                telemetry.addData("Recognition Height %", largestRecog.getHeight() / largestRecog.getImageHeight() * 10);
            }
            telemetry.update();

            tfod.deactivate();

            //Drop the wobble goal...
            telemetry.addLine("Feigned wobble goal drop");
            telemetry.update();
            sleep(2000);

            //Turn towards front picture
            if (TargetZone == 1) { // Tile A
                encoderDrive(robot.DRIVE_SPEED, -24, -24, -24, -24, 5.0); // Reverse 1 tile
                encoderDrive(robot.TURN_SPEED, -24, 24, -24, 24, 5.0); // 90° right turn
                encoderDrive(robot.DRIVE_SPEED, 24, 24, 24, 24, 5.0); // forward 1 tile
            } else if (TargetZone == 3) { // Tile C
                encoderDrive(robot.DRIVE_SPEED, -24, -24, -24, -24, 5.0); // Reverse 1 tile
                encoderDrive(robot.TURN_SPEED, -24, 24, -24, 24, 5.0); // 90° right turn
                encoderDrive(robot.DRIVE_SPEED, -24, -24, -24, -24, 5.0); // Reverse 1 tile
            }

            //Use vuforia to align 1 tile away from Blue Tower Goal Target
            for (int i = 0; i < 5; i++) { // Check if the target is visible 5 times
                sleep(2000);
                if (getVuforia(allTrackables) != null) {
                    break;
                }

                telemetry.addLine("Vuforia Attempt: " + (i + 5));
                telemetry.update();
            }

            //NOTE: 1.5 tiles in the Y axis is in front of the goal
            vuforiaDrive(allTrackables, "turn", false, 0.2, 0, 3);
            vuforiaDrive(allTrackables, "y", true, robot.DRIVE_SPEED,
                    (float) (1.5 * robot.TILE_SIZE), 3); // y switch needs finished


            //Reverse two tiles using encoders to get behind scoring line. Change based on opimal firing distance of launcher
            encoderDrive(robot.DRIVE_SPEED, -1.5 * robot.TILE_SIZE, 7.0);


            //Fire projectiles...
            telemetry.addLine("Feigned Launch");
            telemetry.update();


            // Park on the line
            encoderDrive(robot.DRIVE_SPEED, 0.25 * robot.TILE_SIZE, 7.0);
            drive(0);
            telemetry.addData("Autonomous Complete", "Run Time: " + runtime.toString());
            telemetry.update();

            // END OF LINE
        }
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.4f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }


    public List<Recognition> getTfodRecognitions() {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> recognitions = tfod.getRecognitions();
            if (recognitions != null) {
                //telemetry.addData("# Object Detected", recognitions.size());

                return recognitions;

//                // step through the list of recognitions and display boundary info.
//                int i = 0;
//                for (Recognition recognition : updatedRecognitions) {
//                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
//                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
//                            recognition.getLeft(), recognition.getTop());
//                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
//                            recognition.getRight(), recognition.getBottom());
//                }
            }
        }
        return null;
    }


    public OpenGLMatrix getVuforia(List<VuforiaTrackable> allTrackables) {
        // check all the trackable targets to see which one (if any) is visible.
        targetVisible = false;
        for (VuforiaTrackable trackable : allTrackables) {
            if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                //telemetry.addData("Visible Target", trackable.getName());
                targetVisible = true;

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                if (robotLocationTransform != null) {
                    lastLocation = robotLocationTransform;
                }
                break;
            }
        }

        // Provide feedback as to where the robot is located (if we know).
        if (targetVisible) {
            // express position (translation) of robot in inches.
            VectorF translation = lastLocation.getTranslation();
            telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                    translation.get(0) / mmPerInch, translation.get(1) / mmPerInch, translation.get(2) / mmPerInch);

            // express the rotation of the robot in degrees.
            Orientation rotation = Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
            telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);

            return lastLocation;
        } else {
            telemetry.addData("Visible Target", "none");

            return null;
        }
    }


    public VectorF getVuforiaTranslation(OpenGLMatrix lastLocation) {
        return lastLocation.getTranslation();
    }


    public Orientation getVuforiaRotation(OpenGLMatrix lastLocation) {
        return Orientation.getOrientation(lastLocation, EXTRINSIC, XYZ, DEGREES);
    }


    public void vuforiaDrive(List<VuforiaTrackable> allTrackables,
                             String action, // "x", "y", or "turn"
                             boolean strafe, // to strafe?
                             double speed, // motor speed
                             float target, // target position (x, y, or heading)
                             float tolerance /*How far off is acceptable?*/) {

        float Heading = getVuforiaRotation(getVuforia(allTrackables)).thirdAngle;
        VectorF location = getVuforiaTranslation(getVuforia(allTrackables));

        if (!opModeIsActive()) return;

        switch (action) {
            case "x":
                // do later...
                break;

            case "y":
                if (strafe) Heading -= 90; // when strafing, the right side is the front

                for (int i = 0; i < 5; i++) {
                    if ((Heading > 0/*facing left of 0*/ && location.get(1) > target/*in front of target*/) ||
                            (Heading < 0/*facing right of 0*/ && location.get(1) < target/*in front of target*/)) {
                        // drive backward
                        if (strafe) strafeDrive(speed);
                        else drive(-speed);
                    } else if ((Heading > 0/*facing left of 0*/ && location.get(1) < target/*behind of target*/) ||
                            (Heading < 0/*facing right of 0*/ && location.get(1) > target/*behind target*/)) {
                        // drive forward
                        if (strafe) strafeDrive(speed);
                        else drive(speed);
                    }

                    while ((location.get(1) > target || target < location.get(1))
                            && opModeIsActive()) {
                        location = getVuforiaTranslation(getVuforia(allTrackables));

                        telemetry.addLine("Location X: " + location.get(0));
                        telemetry.addLine("Location Y: " + location.get(1));
                        telemetry.addLine("Target: " + target);
                        telemetry.update();
                        sleep(5);
                    }

                    drive(0);

                    sleep(2000);
                    if (location.get(1) > target || target < location.get(1)) break;
                }

                break;

            case "turn":
                do {
                    if (Heading < target) { // If we are right of the target
                        drive(-speed, speed); // Turn left

                        while (Heading < target
                                && opModeIsActive()) { // Loop while Heading is less than target
                            Heading = getVuforiaRotation(getVuforia(allTrackables)).thirdAngle;
                            telemetry.addLine("Heading: " + Heading);
                            telemetry.addLine("Target: " + target);
                            telemetry.update();
                            sleep(5);
                        }

                    } else if (Heading > target) { // If we are left of the target
                        drive(speed, -speed); // Turn right

                        while (Heading > target
                                && opModeIsActive()) { // Loop while Heading is greater than target
                            telemetry.addLine("Heading: " + Heading);
                            telemetry.addLine("Target: " + target);
                            telemetry.update();
                            sleep(5);
                            Heading = getVuforiaRotation(getVuforia(allTrackables)).thirdAngle;
                        }
                    }

                    drive(0);
                    speed *= 0.8;

                } while ((-tolerance < Heading || Heading < tolerance) && opModeIsActive());
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + action);
        }

        drive(0);
    }


    public void encoderDrive(double speed,
                             double allInches,
                             double timeoutS) {
        encoderDrive(speed, allInches, allInches, allInches, allInches, timeoutS);
    }


    public void encoderDrive(double speed,
                             double leftFrontInches, double rightFrontInches,
                             double leftBackInches, double rightBackInches,
                             double timeoutS) {
        int newLeftFrontTarget;
        int newLeftBackTarget;
        int newRightFrontTarget;
        int newRightBackTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftFrontTarget = robot.leftFront.getCurrentPosition() + (int) (leftFrontInches * robot.COUNTS_PER_INCH);
            newLeftBackTarget = robot.leftBack.getCurrentPosition() + (int) (leftBackInches * robot.COUNTS_PER_INCH);
            newRightFrontTarget = robot.rightFront.getCurrentPosition() + (int) (rightFrontInches * robot.COUNTS_PER_INCH);
            newRightBackTarget = robot.rightBack.getCurrentPosition() + (int) (rightBackInches * robot.COUNTS_PER_INCH);
            robot.leftFront.setTargetPosition(newLeftFrontTarget);
            robot.leftBack.setTargetPosition(newLeftBackTarget);
            robot.rightFront.setTargetPosition(newRightFrontTarget);
            robot.rightBack.setTargetPosition(newRightBackTarget);

            // Turn On RUN_TO_POSITION
            robot.leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftFront.setPower(Math.abs(speed));
            robot.leftBack.setPower(Math.abs(speed));
            robot.rightFront.setPower(Math.abs(speed));
            robot.rightBack.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftFront.isBusy() && robot.rightFront.isBusy() && robot.rightBack.isBusy() && robot.rightFront.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d %7d %7d %7d", newLeftFrontTarget, newLeftBackTarget, newRightFrontTarget, newRightBackTarget);
                telemetry.addData("Path2", "Running at %7d %7d %7d %7d",
                        robot.leftFront.getCurrentPosition(),
                        robot.leftBack.getCurrentPosition(),
                        robot.rightFront.getCurrentPosition(),
                        robot.rightBack.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftFront.setPower(0);
            robot.rightFront.setPower(0);
            robot.leftBack.setPower(0);
            robot.rightBack.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }


    public void strafeDrive(double power) { // Right is positive
        robot.leftFront.setPower(power);
        robot.rightFront.setPower(-power);
        robot.rightBack.setPower(power);
        robot.leftBack.setPower(-power);
    }


    public void drive(double power) {
        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.leftFront.setPower(power);
        robot.rightFront.setPower(power);
        robot.leftBack.setPower(power);
        robot.rightBack.setPower(power);
    }


    public void drive(double leftPower, double rightPower) {
        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.leftFront.setPower(leftPower);
        robot.rightFront.setPower(rightPower);
        robot.leftBack.setPower(leftPower);
        robot.rightBack.setPower(rightPower);
    }


    public void drive(double leftFrontPower, double rightFrontPower,
                      double leftBackPower, double rightBackPower) {
        robot.leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        robot.leftFront.setPower(leftFrontPower);
        robot.rightFront.setPower(rightFrontPower);
        robot.leftBack.setPower(leftBackPower);
        robot.rightBack.setPower(rightBackPower);
    }
}