package com.dating.ui;

import javax.swing.*;
import java.awt.*;

/**
 * 五维行为雷达图 (运动力/文艺力/社交力/探索力/松弛力)
 * 纯手绘，展现底层图形学能力
 */
public class RadarChartPanel extends JPanel {

    private double[] weights; // 5个维度的数据 (0~1)
    private String[] dimensions = {"运动力", "文艺力", "社交力", "探索力", "松弛力"};

    public RadarChartPanel(double[] weights) {
        this.weights = weights;
        setPreferredSize(new Dimension(300, 300));
        setBackground(new Color(245, 245, 245));
    }

    // 更新数据并重绘
    public void updateData(double[] newWeights) {
        this.weights = newWeights;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // 开启抗锯齿，让线条更平滑
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(getWidth(), getHeight()) / 2 - 40;

        // 1. 绘制五边形背景网格
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(new Color(200, 200, 200));
        for (int level = 1; level <= 4; level++) {
            int r = radius * level / 4;
            drawPentagon(g2d, centerX, centerY, r, null, false);
        }

        // 2. 绘制五个轴心线
        g2d.setColor(new Color(180, 180, 180));
        g2d.setStroke(new BasicStroke(1));
        for (int i = 0; i < 5; i++) {
            double angle = Math.PI / 2 - (2 * Math.PI / 5) * i;
            int endX = (int) (centerX + radius * Math.cos(angle));
            int endY = (int) (centerY - radius * Math.sin(angle));
            g2d.drawLine(centerX, centerY, endX, endY);

            // 绘制维度标签
            int labelX = (int) (centerX + (radius + 25) * Math.cos(angle)) - 15;
            int labelY = (int) (centerY - (radius + 25) * Math.sin(angle)) + 5;
            g2d.setColor(new Color(80, 80, 80));
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 11));
            g2d.drawString(dimensions[i], labelX, labelY);
            g2d.setColor(new Color(180, 180, 180));
        }

        // 3. 绘制数据区域 (填充多边形 + 描边)
        if (weights != null && weights.length == 5) {
            int[] xPoints = new int[5];
            int[] yPoints = new int[5];
            for (int i = 0; i < 5; i++) {
                double value = Math.min(1.0, Math.max(0, weights[i])); // 限幅0~1
                double angle = Math.PI / 2 - (2 * Math.PI / 5) * i;
                int r = (int) (radius * value);
                xPoints[i] = (int) (centerX + r * Math.cos(angle));
                yPoints[i] = (int) (centerY - r * Math.sin(angle));
            }

            // 填充半透明蓝色区域
            g2d.setColor(new Color(66, 133, 244, 100));
            g2d.fillPolygon(xPoints, yPoints, 5);

            // 描边
            g2d.setColor(new Color(66, 133, 244));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawPolygon(xPoints, yPoints, 5);

            // 绘制数据点 (小圆点)
            g2d.setColor(new Color(255, 100, 100));
            for (int i = 0; i < 5; i++) {
                g2d.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
            }
        }
    }

    // 辅助方法：画正五边形
    private void drawPentagon(Graphics2D g2d, int cx, int cy, int r, Color color, boolean fill) {
        int[] x = new int[5];
        int[] y = new int[5];
        for (int i = 0; i < 5; i++) {
            double angle = Math.PI / 2 - (2 * Math.PI / 5) * i;
            x[i] = (int) (cx + r * Math.cos(angle));
            y[i] = (int) (cy - r * Math.sin(angle));
        }
        if (fill && color != null) {
            g2d.setColor(color);
            g2d.fillPolygon(x, y, 5);
        } else {
            g2d.drawPolygon(x, y, 5);
        }
    }
}