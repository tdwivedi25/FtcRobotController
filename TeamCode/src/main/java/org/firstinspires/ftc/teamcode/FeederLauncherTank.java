package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Feeder + Launcher (Tank, Forward Fixed)", group="Linear OpMode")
public class FeederLauncherTank extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // drivetrain
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    // launcher and feeders
    private DcMotor launcher = null;
    private Servo leftFeeder = null;
    private Servo rightFeeder = null;

    @Override
    public void runOpMode() {

        // --- Hardware mapping: names MUST match your Control Hub configuration ---
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        launcher   = hardwareMap.get(DcMotor.class, "launcher");
        leftFeeder = hardwareMap.get(Servo.class, "left_feeder");
        rightFeeder= hardwareMap.get(Servo.class, "right_feeder");

        // --- Motor directions ---
        // Set so that pushing the left/right sticks forward (stick value negative)
        // results in positive motor power (robot moves forward).
        // If your robot still goes backwards, swap these directions.
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        // launcher direction: adjust if your physical wiring makes it spin opposite
        launcher.setDirection(DcMotor.Direction.FORWARD);

        // optional: set initial servo positions (adjust values to your hardware)
        leftFeeder.setPosition(0.0);   // closed
        rightFeeder.setPosition(1.0);  // closed (mirrored)

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            // --- DRIVE (tank controls) ---
            // gamepad stick: pushing forward returns negative values, so we invert them.
            double leftPower  = -gamepad1.left_stick_y;
            double rightPower = -gamepad1.right_stick_y;

            // optional: clip to [-1, 1] (not usually necessary, but safe)
            leftPower  = Math.max(-1.0, Math.min(1.0, leftPower));
            rightPower = Math.max(-1.0, Math.min(1.0, rightPower));

            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);

            // --- LAUNCHER control ---
            // A: spin launcher at full power; B: stop launcher;
            if (gamepad1.a) {
                launcher.setPower(1.0);
            } else if (gamepad1.b) {
                launcher.setPower(0.0);
            }

            // --- FEEDER control ---
            // X: open feeders to feed a ring/ball (adjust positions to your mechanism)
            // Y: close feeders (return to default)
            if (gamepad1.x) {
                leftFeeder.setPosition(1.0);   // open (example)
                rightFeeder.setPosition(0.0);
            } else if (gamepad1.y) {
                leftFeeder.setPosition(0.0);   // closed (example)
                rightFeeder.setPosition(1.0);
            }

            // --- TELEMETRY ---
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Drive", "L (%.2f) R (%.2f)", leftPower, rightPower);
            telemetry.addData("Launcher Power", "%.2f", launcher.getPower());
            telemetry.addData("LeftFeeder", "%.2f", leftFeeder.getPosition());
            telemetry.addData("RightFeeder", "%.2f", rightFeeder.getPosition());
            telemetry.update();
        }
    }
}
