<h1>Computer simulator</h1>
<p>This is the simple <i><strong>computer system simulator</strong></i> which has started as a course project on the Programming course in Peter the Greate Saint Petersburg State Polytechnic University.<br></p>
<hr />
<h3>User interface</h3>
<img src="screenshots/main_window.png" alt="Main window">
<h3>Instructions Table</h3>
<p>Here's a set of possible mnemonics for the machine codes that the computer recognizes:</p>
<table>
  <tr>
    <td><b>Operation</b></td>
    <td><b>Code</b></td>
    <td><b>Mnemonic</b></td>
  </tr>
  <tr>
    <td>Load</td>
    <td>0b0001</td>
    <td>LOD</td>
  </tr>
  <tr>
    <td>Store</td>
    <td>0b1000</td>
    <td>STO</td>
  </tr>
  <tr>
    <td>Add</td>
    <td>0b0010</td>
    <td>ADD</td>
  </tr>
  <tr>
    <td>Substract</td>
    <td>0b0011</td>
    <td>SUB</td>
  </tr>
  <tr>
    <td>Add with Carry</td>
    <td>0b0100</td>
    <td>ADC</td>
  </tr>
  <tr>
    <td>Substract with Borrow</td>
    <td>0b0101</td>
    <td>SBB</td>
  </tr>
  <tr>
    <td>Jump</td>
    <td>0b0110</td>
    <td>JMP</td>
  </tr>
  <tr>
    <td>Jump If Zero</td>
    <td>0b1010</td>
    <td>JZ</td>
  </tr>
  <tr>
    <td>Jump If Not Zero</td>
    <td>0b1001</td>
    <td>JNZ</td>
  </tr>
  <tr>
    <td>Halt</td>
    <td>0b0111</td>
    <td>HLT</td>
  </tr>
</table>
<h3>Examples</h3>
<p>There are examples of programs for the simulator below:</p>
<h6>First example:</h6>
<pre>
;  Multiply 8-bit values 0xA7 and 0x1C
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
RESULT: 00, 00            ; 16-bit value goes here
</pre>
<h6>Second example</h6>
<pre>
; Add two 8-bit numbers together and subtract a third
       LOD A, [NUM1]
       ADD A, [NUM1+1]
       SUB A, [NUM1+2]
       STO [SAVE], A
       HLT

NUM1: 45, A9, 8E
SAVE: 00 ; Final result goes here
</pre>
<b>Don't try to place labels before or between instructions.</b> It doesn't work temporarily. Type labels only after all commands.
