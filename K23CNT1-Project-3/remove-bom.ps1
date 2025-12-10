# PowerShell script to remove BOM from Java files

$projectPath = "c:\Project 3\NguyenNgocHieu-K23CNT1-Project03-2310900037\K23CNT1-Project-3\src\main\java"

# Get all Java files
$javaFiles = Get-ChildItem -Path $projectPath -Filter "*.java" -Recurse

foreach ($file in $javaFiles) {
    $bytes = [System.IO.File]::ReadAllBytes($file.FullName)
    
    # Check for UTF-8 BOM (EF BB BF)
    if ($bytes.Length -ge 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
        Write-Host "Removing BOM from: $($file.FullName)"
        $newBytes = $bytes[3..($bytes.Length - 1)]
        [System.IO.File]::WriteAllBytes($file.FullName, $newBytes)
    }
}

Write-Host "Done! BOM removed."
