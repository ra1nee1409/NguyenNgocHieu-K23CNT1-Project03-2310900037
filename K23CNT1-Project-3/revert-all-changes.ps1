# PowerShell script to REVERT all method calls back to original
# This reverses the previous changes

$projectPath = "c:\Project 3\NguyenNgocHieu-K23CNT1-Project03-2310900037\K23CNT1-Project-3\src\main\java"

# Get all Java files
$javaFiles = Get-ChildItem -Path $projectPath -Filter "*.java" -Recurse

foreach ($file in $javaFiles) {
    $content = Get-Content -Path $file.FullName -Raw -Encoding UTF8
    $originalContent = $content
    
    # REVERT method calls back to original
    $content = $content -replace '\.getUser\(', '.getNnhUser('
    $content = $content -replace '\.setUser\(', '.setNnhUser('
    $content = $content -replace '\.getProduct\(', '.getNnhProduct('
    $content = $content -replace '\.setProduct\(', '.setNnhProduct('
    $content = $content -replace '\.getOrderItems\(', '.getNnhOrderItems('
    $content = $content -replace '\.setOrderItems\(', '.setNnhOrderItems('
    
    # REVERT builder methods
    $content = $content -replace '\.user\(', '.nnhUser('
    $content = $content -replace '\.product\(', '.nnhProduct('
    $content = $content -replace '\.orderItems\(', '.nnhOrderItems('
    
    # REVERT repository methods
    $content = $content -replace 'findByUser_Id', 'findByUserId'
    $content = $content -replace 'deleteByUser_Id', 'deleteByUserId'
    $content = $content -replace 'findByUser_IdAndProduct_Id', 'findByUserIdAndProductId'
    $content = $content -replace 'countByUser_Id', 'countByUserId'
    
    # Only write if content changed
    if ($content -ne $originalContent) {
        Write-Host "Reverting: $($file.FullName)"
        [System.IO.File]::WriteAllText($file.FullName, $content, [System.Text.Encoding]::UTF8)
    }
}

Write-Host "Done! All changes have been reverted."
