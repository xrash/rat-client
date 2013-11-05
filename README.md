# Rat android client

Rat transforms your phone into a touchpad. For get it working you will need a server running on your computer and a client running on your phone. This is the client for Android.

Plug your Android phone into your computer and enable the developer debug mode, then run:

    make install

Below is the protocol used:

    [ byte sequence ]
        action

* [ 0x00 ]
    > CLOSE SERVER

* [ 0x10 ]
    > LEFT MOUSE BUTTON CLICK

* [ 0x11 ]
    > LEFT MOUSE BUTTON PRESS

* [ 0x12 ]
    > LEFT MOUSE BUTTON RELEASE

* [ 0x20 ]
    > RIGHT MOUSE BUTTON CLICK

* [ 0x21 ]
    > RIGHT MOUSE BUTTON PRESS

* [ 0x22 ]
    > RIGHT MOUSE BUTTON RELEASE

* [ 0x30, DY, DY ]
    > MOUSE WHEEL UP

* [ 0x31, DY, DY ]
    > MOUSE WHEEL DOWN

* [ 0x40, DX, DX, DY, DY ]
    > MOUSE MOVE

* [ 0XFF ]
    > PING

