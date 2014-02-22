Attribute VB_Name = "Module1"
Option Explicit

Public fragm As String      '-- ����� ��� ���������
Public subst As String      '-- ����� ��� ���������
Public resBuf As String     '-- ����� ��� ����������
Public errBuf As String     '-- ����� ��� �������� ������
Public colData As Integer   '-- ����� ������� ������
Public colCmd As Integer    '-- ����� ������� ������
Public rowCmdBeg As Integer '-- ����� ������ ������ ������
Public rowCmdEnd As Integer '-- ����� ��������� ������ ������
Public rowFragm As Integer  '-- ����� ������ ���������
Public result As Integer    '-- ��������� ���������� �������
Public substNom As Integer  '-- ����� ���������
Public rowButton As Integer '-- ����� ������ � �������


Public Declare Function getErrMes Lib "d:\dbsn.dll" (ByVal dbhadr As Long, ByVal err_buf As String, ByVal buf_len As Long, ByVal comment As String) As Long
Public Declare Function addSubst Lib "d:\dbsn.dll" (ByVal pc As String, ByVal pcres As String, ByVal res_size As Long, ByVal pcsubst As String) As Long
Public Declare Function cutSubst Lib "d:\dbsn.dll" (ByVal pc As String, ByVal nom_sub As Long) As Long
Public Declare Function clearSubst Lib "d:\dbsn.dll" (ByVal pc As String, ByVal nom_sub As Long) As Long
Public Declare Function insSubst Lib "d:\dbsn.dll" (ByVal pc As String, ByVal pcres As String, ByVal res_size As Long, ByVal nom_sub As Long, ByVal pcsubst As String) As Long
Public Declare Function uniteSubst Lib "d:\dbsn.dll" (ByVal pc As String, ByVal nom_sub As Long) As Long
Public Declare Function substLen Lib "d:\dbsn.dll" (ByVal pc As String, ByVal nom_sub As Long) As Long
Public Declare Function substCount Lib "d:\dbsn.dll" (ByVal bStr As String) As Long
Public Declare Function getSubst Lib "d:\dbsn.dll" (ByVal pc As String, ByVal pcbuf As String, ByVal buf_size As Long, ByVal nom_sub As Long) As Long
Public Declare Function cutLeft Lib "d:\dbsn.dll" (ByVal pc As String, ByVal nom_sub As Long) As Long
Public Declare Function cutRight Lib "d:\dbsn.dll" (ByVal pc As String, ByVal nom_sub As Long) As Long



'== ����������� ���� � ������
Sub SheetPrep()
    colCmd = 2:  rowCmdBeg = 3:  rowCmdEnd = 13
    colData = 6: rowFragm = 3
    lightDownAll
    resBuf = String$(4000, "*")
    errBuf = String$(4000, "*")
End Sub

'== ����� ��������� �� ���� ������
Sub lightDownAll()
    Dim rowCmd As Integer
    For rowCmd = rowCmdBeg To rowCmdEnd
        Cells(rowCmd, colCmd).Select
        Selection.Interior.ColorIndex = xlNone
    Next
End Sub

'-- ���������� ������ �������( ������� �������� ����������)
Sub setYellowColor(rowSel)
    Cells(rowSel, colCmd).Select
    With Selection.Interior
        .ColorIndex = 27
        .Pattern = xlSolid
        .PatternColorIndex = xlAutomatic
    End With
End Sub

'-- ���������� ������� ������� ( ������� ���������� � �������)
Sub setRedColor(rowSel)
    Cells(rowSel, colCmd).Select
    With Selection.Interior
        .ColorIndex = 22
        .Pattern = xlSolid
        .PatternColorIndex = xlAutomatic
    End With
End Sub

'-- ����� ��������� � �������
Sub unsetColor(rowSel)
    Cells(rowSel, colCmd).Select
    Selection.Interior.ColorIndex = xlNone
End Sub

'-- �������� ������ �����������
Sub clearResults()
    Dim rowRes As Integer
    For rowCmd = rowFragm + 1 To rowCmdEnd
        Cells(rowCmd, colCmd) = ""
    Next
End Sub

'-- �������� ����� ���������
Sub addSubstB()
    SheetPrep
    rowButton = 4
    fragm = Cells(rowFragm, colData).Text
    subst = Cells(rowFragm + 1, colData).Text
    result = addSubst(fragm, resBuf, 2000, subst)
    Cells(rowFragm + 4, colData) = result
    If result >= 0 Then
        Cells(rowFragm, colData) = resBuf
        Cells(rowFragm + 5, colData) = ""
        setYellowColor rowButton
    Else
        result = getErrMes(result, errBuf, 2000, "�� ���� �������� ���������")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If
End Sub
'-- ������� ���������
Sub cutSubstB()
    SheetPrep
    rowButton = 5
    fragm = Cells(rowFragm, colData).Text
    substNom = Cells(rowFragm + 2, colData).Value
    result = cutSubst(fragm, substNom)
    Cells(rowFragm + 4, colData) = result
    If result >= 0 Then
        Cells(rowFragm, colData) = fragm
        Cells(rowFragm + 5, colData) = ""
        setYellowColor rowButton
    Else
        result = getErrMes(result, errBuf, 2000, "�� ���� ������� ���������")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub

'-- ��������� ����� ��������
Sub countSubstB()
    SheetPrep
    rowButton = 6
    fragm = Cells(rowFragm, colData).Text
    result = substCount(fragm)
    Cells(rowFragm + 4, colData) = result
    If result >= 0 Then
        Cells(rowFragm + 3, colData) = result
        Cells(rowFragm + 5, colData) = ""
        setYellowColor rowButton
    Else
        result = getErrMes(result, errBuf, 2000, "�� ���� ��������� ����� ��������")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub

'-- �������� ����� ���������
Sub insSubstB()
    SheetPrep
    rowButton = 7
    fragm = Cells(rowFragm, colData).Text
    subst = Cells(rowFragm + 1, colData).Text
    substNom = Cells(rowFragm + 2, colData).Value
    result = insSubst(fragm, resBuf, 2000, substNom, subst)
    Cells(rowFragm + 4, colData) = result
    If result >= 0 Then
        Cells(rowFragm, colData) = resBuf
        Cells(rowFragm + 3, colData) = result
        Cells(rowFragm + 5, colData) = ""
        setYellowColor rowButton
    Else
        result = getErrMes(result, errBuf, 2000, "�� ���� �������� ���������")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub

'-- �������� ��������� �� �����
Sub setSubstB()
    SheetPrep
    rowButton = 8
    fragm = Cells(rowFragm, colData).Text
    subst = Cells(rowFragm + 1, colData).Text
    substNom = Cells(rowFragm + 2, colData).Value
    result = setSubst(fragm, resBuf, 2000, substNom, subst)
    Cells(rowFragm + 4, colData) = result
    If result >= 0 Then
        Cells(rowFragm, colData) = resBuf
        Cells(rowFragm + 3, colData) = result
        Cells(rowFragm + 5, colData) = ""
        setYellowColor rowButton
    Else
        result = getErrMes(result, errBuf, 2000, "�� ���� �������� ���������")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub

'-- �������� ���������
Sub clearSubstB()
    SheetPrep
    rowButton = 9
    fragm = Cells(rowFragm, colData).Text
    substNom = Cells(rowFragm + 2, colData).Value
    result = clearSubst(fragm, substNom)
    Cells(rowFragm + 4, colData) = result
    If result >= 0 Then
        Cells(rowFragm, colData) = fragm
        Cells(rowFragm + 3, colData) = result
        Cells(rowFragm + 5, colData) = ""
        setYellowColor rowButton
    Else
        result = getErrMes(result, errBuf, 2000, "�� ���� �������� ���������")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub


'-- ���������� ������ � ����������
Sub uniteSubstB()
    SheetPrep
    rowButton = 10
    fragm = Cells(rowFragm, colData).Text
    substNom = Cells(rowFragm + 2, colData).Value
    result = uniteSubst(fragm, substNom)
    Cells(rowFragm + 4, colData) = result
    If result >= 0 Then
        Cells(rowFragm, colData) = fragm
        Cells(rowFragm + 3, colData) = result
        Cells(rowFragm + 5, colData) = ""
        setYellowColor rowButton
    Else
        result = getErrMes(result, errBuf, 2000, "�� ���� ���������� ��������� � ����������")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub


'-- �������� ����� ���������
Sub getSubstLenB()
    SheetPrep
    rowButton = 11
    fragm = Cells(rowFragm, colData).Text
    substNom = Cells(rowFragm + 2, colData).Value
    result = substLen(fragm, substNom)
    Cells(rowFragm + 4, colData) = result
    If result >= 0 Then
        'Cells(rowFragm, colData) = fragm
        Cells(rowFragm + 3, colData) = result
        Cells(rowFragm + 5, colData) = ""
        setYellowColor rowButton
    Else
        result = getErrMes(result, errBuf, 2000, "�� ���� �������� ����� ���������")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If


End Sub

'-- �������� ��������� �� �� ������
Sub getSubstB()
    SheetPrep
    rowButton = 12
    fragm = Cells(rowFragm, colData).Text
    substNom = Cells(rowFragm + 2, colData).Value
    result = getSubst(fragm, resBuf, 2000, substNom)
    Cells(rowFragm + 4, colData) = result
    If result >= 0 Then
        Cells(rowFragm + 1, colData) = resBuf
        Cells(rowFragm + 3, colData) = result
        Cells(rowFragm + 5, colData) = ""
        setYellowColor rowButton
    Else
        Cells(rowFragm + 1, colData) = ""
        result = getErrMes(result, errBuf, 2000, "�� ���� �������� ���������")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If
End Sub


'-- ������� ������ ����� ������
Sub getLeftB()
    SheetPrep
    rowButton = 13
    fragm = Cells(rowFragm, colData).Text
    substNom = Cells(rowFragm + 2, colData).Value
    result = cutRight(fragm, substNom)
    Cells(rowFragm + 4, colData) = result
    If result >= 0 Then
        Cells(rowFragm, colData) = fragm
        Cells(rowFragm + 3, colData) = result
        Cells(rowFragm + 5, colData) = ""
        setYellowColor rowButton
    Else
        'Cells(rowFragm + 1, colData) = ""
        result = getErrMes(result, errBuf, 2000, "�� ���� �������� ������ ����� ���������")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub

'-- ������� ����� ����� ������
Sub cutLeftB()
    SheetPrep
    rowButton = 14
    fragm = Cells(rowFragm, colData).Text
    substNom = Cells(rowFragm + 2, colData).Value
    result = cutLeft(fragm, substNom)
    Cells(rowFragm + 4, colData) = result
    If result >= 0 Then
        Cells(rowFragm, colData) = fragm
        Cells(rowFragm + 3, colData) = result
        Cells(rowFragm + 5, colData) = ""
        setYellowColor rowButton
    Else
        'Cells(rowFragm + 1, colData) = ""
        result = getErrMes(result, errBuf, 2000, "�� ���� �������� ����� ����� ���������")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub





Private Sub Workbook_SheetBeforeDoubleClick(ByVal Sh As Object, ByVal Target As Range, Cancel As Boolean)
    Dim CommandName As String
    Dim CommandRow, CommandCol As Integer
   ' Dim SubstNom As Integer
    CommandRow = ActiveCell.Row
    CommandCol = ActiveCell.Column
    CommandName = Trim(ActiveCell.Text)
    
    If Prepare And Len(CommandName) > 2 Then
        HideColor       '--- ������� ���������
        Select Case CommandName
            Case "�������� ������"
                Call ClearCells
            Case "������� ���������"
                Call readFragmDBSN
            Case "��������� ����� ��������"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call CountSubstDBSN(substNom)
                substNom = 0
            Case "�������� ���������"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call AddSubstDBSN(substNom)
                substNom = 0
            Case "�������� ���������"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call ClearSubstDBSN(substNom)
            Case "������� ���������"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call CutSubstDBSN(substNom)
            Case "�������� ����� ���������"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call InsSubstDBSN(substNom)
            Case "���������� ������ � ����������"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call UnitSubstDBSN(substNom)
            Case "��������� ����� ���������"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call SubstLenDBSN(substNom)
            Case "���������� ����� ��������"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call SetSubstDBSN(substNom)
        End Select
        Sheets("cmd").Select
        Cells(3, 4).Select
    End If
    
End Sub

