Attribute VB_Name = "Module1"
Option Explicit

Public fragm As String      '-- буфер для фрагмента
Public subst As String      '-- буфер для подстроки
Public resBuf As String     '-- буфер для результата
Public errBuf As String     '-- буфер для описания ошибки
Public colData As Integer   '-- номер колонки двнных
Public colCmd As Integer    '-- номер колонки команд
Public rowCmdBeg As Integer '-- номер строки начала команд
Public rowCmdEnd As Integer '-- номер последней строка команд
Public rowFragm As Integer  '-- номер строки фрагмента
Public result As Integer    '-- результат выполнения функции
Public substNom As Integer  '-- номер подстроки
Public rowButton As Integer '-- номер строки с кнопкой


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



'== подготовить лист к работе
Sub SheetPrep()
    colCmd = 2:  rowCmdBeg = 3:  rowCmdEnd = 13
    colData = 6: rowFragm = 3
    lightDownAll
    resBuf = String$(4000, "*")
    errBuf = String$(4000, "*")
End Sub

'== снять подсветку со всех команд
Sub lightDownAll()
    Dim rowCmd As Integer
    For rowCmd = rowCmdBeg To rowCmdEnd
        Cells(rowCmd, colCmd).Select
        Selection.Interior.ColorIndex = xlNone
    Next
End Sub

'-- подсветить желтым команду( признак удачного выполнения)
Sub setYellowColor(rowSel)
    Cells(rowSel, colCmd).Select
    With Selection.Interior
        .ColorIndex = 27
        .Pattern = xlSolid
        .PatternColorIndex = xlAutomatic
    End With
End Sub

'-- подсветить красным команду ( признак выполнения с ошибкой)
Sub setRedColor(rowSel)
    Cells(rowSel, colCmd).Select
    With Selection.Interior
        .ColorIndex = 22
        .Pattern = xlSolid
        .PatternColorIndex = xlAutomatic
    End With
End Sub

'-- снять подсветку с команды
Sub unsetColor(rowSel)
    Cells(rowSel, colCmd).Select
    Selection.Interior.ColorIndex = xlNone
End Sub

'-- очистить ячейки результатов
Sub clearResults()
    Dim rowRes As Integer
    For rowCmd = rowFragm + 1 To rowCmdEnd
        Cells(rowCmd, colCmd) = ""
    Next
End Sub

'-- Добавить новую подстроку
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
        result = getErrMes(result, errBuf, 2000, "не смог добавить подстроку")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If
End Sub
'-- Удалить подстроку
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
        result = getErrMes(result, errBuf, 2000, "не смог удалить подстроку")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub

'-- Сосчитать число подстрок
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
        result = getErrMes(result, errBuf, 2000, "не смог сосчитать число подстрок")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub

'-- Вставить новую подстроку
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
        result = getErrMes(result, errBuf, 2000, "не смог вставить подстроку")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub

'-- Заменить подстроку на новую
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
        result = getErrMes(result, errBuf, 2000, "не смог заменить подстроку")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub

'-- Очистить подстроку
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
        result = getErrMes(result, errBuf, 2000, "не смог очистить подстроку")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub


'-- Объединить строку с предидущей
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
        result = getErrMes(result, errBuf, 2000, "не смог объединить подстроку с предидущей")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub


'-- Получить длину подстроки
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
        result = getErrMes(result, errBuf, 2000, "не смог получить длину подстроки")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If


End Sub

'-- Получить подстроку по ее номеру
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
        result = getErrMes(result, errBuf, 2000, "не смог получить подстроку")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If
End Sub


'-- Удалить правую часть строки
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
        result = getErrMes(result, errBuf, 2000, "не смог отрезать правую часть фрагмента")
        Cells(rowFragm + 5, colData) = errBuf
        setRedColor rowButton
    End If

End Sub

'-- Удалить левую часть строки
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
        result = getErrMes(result, errBuf, 2000, "не смог отрезать левую часть фрагмента")
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
        HideColor       '--- снимаем подсветку
        Select Case CommandName
            Case "Очистить ячейки"
                Call ClearCells
            Case "Считать фрагменты"
                Call readFragmDBSN
            Case "Сосчитать число подстрок"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call CountSubstDBSN(substNom)
                substNom = 0
            Case "Добавить подстроку"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call AddSubstDBSN(substNom)
                substNom = 0
            Case "Очистить подстроку"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call ClearSubstDBSN(substNom)
            Case "Удалить подстроку"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call CutSubstDBSN(substNom)
            Case "Вставить новую подстроку"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call InsSubstDBSN(substNom)
            Case "Объединить строку с предидущей"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call UnitSubstDBSN(substNom)
            Case "Расчитать длину подстроки"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call SubstLenDBSN(substNom)
            Case "Установить новое значение"
                substNom = CInt(Sheets("cmd").Cells(5, 4).Text)
                Call SetSubstDBSN(substNom)
        End Select
        Sheets("cmd").Select
        Cells(3, 4).Select
    End If
    
End Sub

