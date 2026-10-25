<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#0A0A0A"
    android:orientation="vertical"
    android:padding="16dp">

    <!-- স্ট্যাটাস বার: সাব-সিস্টেম স্ট্যাটাস দেখানোর জন্য -->
    <TextView
        android:id="@+id/tvStatusBar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="[ ULTRON SUPER AI : 65 SUB-SYSTEMS ONLINE ]"
        android:textColor="#00FF66"
        android:textSize="14sp"
        android:textStyle="bold"
        android:paddingBottom="8dp" />

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="#00FF66"
        android:layout_marginBottom="12dp"/>

    <!-- কনসোল লগ: AI এর সাথে কথা বলা বা মেসেজ দেখানোর স্ক্রিন -->
    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:background="#121212"
        android:padding="12dp"
        android:fillViewport="true">

        <TextView
            android:id="@+id/tvConsoleLog"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="> Booting ULTRON Core Matrix...\n> Voice Biometric: Armed (Boss Only Mode).\n> Language Protocol: Bengali (Default).\n\n"
            android:textColor="#00FFCC"
            android:textSize="13sp"
            android:fontFamily="monospace" />
    </ScrollView>

    <!-- কমান্ড লেখার বক্স এবং বাটন -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:layout_marginTop="12dp">

        <EditText
            android:id="@+id/etCommandInput"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:background="#1E1E1E"
            android:hint="Type command, Boss..."
            android:textColor="#FFFFFF"
            android:textColorHint="#777777"
            android:textSize="14sp"
            android:padding="12dp"
            android:inputType="text" />

        <Button
            android:id="@+id/btnExecute"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:backgroundTint="#00FF66"
            android:text="EXECUTE"
            android:textColor="#000000"
            android:textStyle="bold" />
    </LinearLayout>

</LinearLayout>
