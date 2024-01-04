package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.opmodes.AutoBase;

@Config
@Autonomous
public class AutoFindServo extends LinearOpMode {
    public static double servo0 = 0.0;
    Servo s0;

    @Override
    public void runOpMode() throws InterruptedException {
        s0 = hardwareMap.servo.get("0");
        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            s0.setPosition(servo0);
        }
    }
}
