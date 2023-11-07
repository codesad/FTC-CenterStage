package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.opmodes.AutoBase;

@Autonomous
@Config
public class AutoTest2 extends AutoBase {
    Servo servo1, servo2;
    public static double servoPos1 = 0, servoPos2 = 0;

    @Override
    public void onInit() {
        servo1 = hardwareMap.servo.get("0");
        servo1.setDirection(Servo.Direction.REVERSE);
        servo2 = hardwareMap.servo.get("1");
    }

    @Override
    public void onStartTick() {
        servo1.setPosition(servoPos1);
        servo2.setPosition(servoPos2);
    }
}
