<h1>Computer simulator</h1>
<p>This is the simple <i><strong>computer system simulator</strong></i> which has started as a course project on the Programming course in Saint Petersburg State Polytechnical University.<br>
<hr />
There are examples of programs for the simulator below:</p>
<h6>First example:</h6>
<pre>
;  Multiply 0xA7 and 0x1C
BEGIN:  LOD A, [RESULT+1] ; Load  byte at address RESULT+1 into accumulator
        ADD A, [NUM1+1]   ; Add byte at address NUM1+1 to accumulator
        STO [RESULT+1], A ; Store contents of accumulator at address RESULT+1

        LOD A, [RESULT]   ; Load  byte at address RESULT into accumulator
        ADC A, [NUM1]     ; Add with carry byte at address NUM1 to accumulator
        STO [RESULT], A   ; Store contents of accumulator at address RESULT

        LOD A, [NUM2+1]   ; Load  byte at address NUM2+1 into accumulator
        ADD A, [DEC]      ; Add byte at address DEC to accumulator
        STO [NUM2+1], A   ; Store contents of accumulator at address NUM2+1

        JNZ BEGIN         ; Jump if not zero to the instruction at BEGIN
        HLT               ; Halt
        
DEC:    FF                ; On the 28 (0x1C) time through value 1 will be added
                          ;  to 0xFF and the result will be zero, so
                          ;  the JNZ instruction will not jump back to BEGIN
                          ;  Instead, the next instruction is a Halt.

NUM1:   00, A7
NUM2:   00, 1C
RESULT: 00, 00
</pre>
<h6>Second example</h6>
<pre>
; Add 0x45 and 0xA9, then substract 0x8E
  LOD A, [NUM1]
  ADD A, [NUM1+1]
  SUB A, [NUM1+2]
  STO [SAVE], A
  HLT

NUM1:	45, A9, 8E
SAVE:	00
</pre>
<b>Don't try place labels before or between instructions.</b> It doesn't work temporarily. Type labels only after all commands.
