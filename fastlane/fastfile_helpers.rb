 
def fastfile_dir
File.expand_path(__dir__)
end

def project_path
return File.expand_path("#{fastfile_dir}/..")
end
