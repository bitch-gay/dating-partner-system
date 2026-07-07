package com.dating.ui;

import javax.swing.*;
import java.awt.*;

public class RadarChartPanel extends JPanel {
    private double[] weights = {0.6, 0.6, 0.6, 0.6, 0.6};
    private String[] dimensions = {"运动力", "文艺力", "社交力", "探索力", "松弛力"};

    public RadarChartPanel(double[] weights) {
        if (weights != null && weights.length == 5) this.weights = weights;
        setPreferredSize(new Dimension(280, 280));
        setBackground(Color.WHITE);
    }

    public void updateData(double[] newWeights) {
        if (newWeights != null && newWeights.length == 5) {
            this.weights = newWeights;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        int cx = w / 2, cy = h / 2;
        int radius = Math.min(w, h) / 2 - 40;

        if (radius <= 0) return;

        // 画网格
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 1; i <= 4; i++) {
            int r = radius * i / 4;
            drawPentagon(g2d, cx, cy, r, false, null);
        }

        // 画轴
        g2d.setColor(Color.GRAY);
        for (int i = 0; i < 5; i++) {
            double angle = Math.PI / 2 - (2 * Math.PI / 5) * i;
            int ex = (int) (cx + radius * Math.cos(angle));
            int ey = (int) (cy - radius * Math.sin(angle));
            g2d.drawLine(cx, cy, ex, ey);
            // 标签
            int lx = (int) (cx + (radius + 20) * Math.cos(angle)) - 15;
            int ly = (int) (cy - (radius + 20) * Math.sin(angle)) + 5;
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 11));
            g2d.drawString(dimensions[i], lx, ly);
            g2d.setColor(Color.GRAY);
        }

        // 画数据
        int[] xs = new int[5], ys = new int[5];
        for (int i = 0; i < 5; i++) {
            double val = Math.min(1.0, Math.max(0, weights[i]));
            double angle = Math.PI / 2 - (2 * Math.PI / 5) * i;
            int r = (int) (radius * val);
            xs[i] = (int) (cx + r * Math.cos(angle));
            ys[i] = (int) (cy - r * Math.sin(angle));
        }
        g2d.setColor(new Color(66, 133, 244, 100));
        g2d.fillPolygon(xs, ys, 5);
        g2d.setColor(new Color(66, 133, 244));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawPolygon(xs, ys, 5);
    }

    private void drawPentagon(Graphics2D g2d, int cx, int cy, int r, boolean fill, Color color) {
        int[] x = new int[5], y = new int[5];
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